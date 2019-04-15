import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm
    {

    private ArrayList<Chromosome> chromosomes;
    private ArrayList<Chromosome> choosedChromosomes;
    private ArrayList<Double> probability;
    private ArrayList<Integer> value;
    private Double totalInverse = 0D;
    private Random generator;
    private Boolean isDone = false;
    private Chromosome theBestChromosome = null;
    private Double MUTATION_RADIO = 0.1;
    private Integer MAX_VALUE_DURING_MUTATION = 10;
    Socket socket;
    Scanner readFromServer;
    PrintWriter writeToServer;


    public GeneticAlgorithm(ArrayList<Chromosome> chromosomes)
        {
        this.chromosomes = chromosomes;
        generator = new Random();
        socket = new Socket();
        }

    public void exec()
        {
        openConetionWithChart();

        while (!isDone)
            {

            sort();
            printChromosomes();
            calculateTotalInverse();
            if (!isDone)
                {
                calculateProbability();
                chooseChromosome();
                swapChromosomes();
                mutation();
                }
            }

        offConetionWithChart();
        }

    public void sort()
        {

        System.out.println("-------------------------------------------");

        System.out.println(chromosomes);

        chromosomes.sort((o1, o2) ->
                {
                if (o1.getRosenbrockFunctionEvaulate() > o2.getRosenbrockFunctionEvaulate())
                    {
                    return 1;
                    }

                if (o1.getRosenbrockFunctionEvaulate() == o2.getRosenbrockFunctionEvaulate())
                    {
                    return 0;
                    }

                return -1;
                }
        );
        System.out.println("-------------------------------------------");
        System.out.println(chromosomes);
        }

    private void calculateTotalInverse()
        {
        totalInverse = 0.0;
        for (Chromosome chromosome : chromosomes)
            {
            if (chromosome.getRosenbrockFunctionEvaulate() != 0.0 && isDone == false)
                {
                totalInverse += 1.0 / chromosome.getRosenbrockFunctionEvaulate();
                }
            else if (isDone == false)
                {
                isDone = true;
                theBestChromosome = chromosome;
                }
            }

        }

    private void calculateProbability()
        {
        probability = new ArrayList<>();
        for (Chromosome chromosome : chromosomes)
            {
            probability.add(((1.0 / chromosome.getRosenbrockFunctionEvaulate()) / totalInverse));
            }
        }

    private void chooseChromosome()
        {
        choosedChromosomes = new ArrayList<>();
        System.out.println("-------------------------------------------");
        for (Chromosome chromosome : chromosomes)
            {
            choosedChromosomes.add(chromosomes.get(getNumberForChooseChromosome()));
            System.out.println(chromosomes.get(getNumberForChooseChromosome()));
            }
        }

    private Integer getNumberForChooseChromosome()
        {
        Double rand = generator.nextDouble();
        Integer numberOfChromosome = 0;
        rand -= probability.get(numberOfChromosome);
        while (rand > 0.0)
            {
            numberOfChromosome++;
            rand -= probability.get(numberOfChromosome);
            }
        return numberOfChromosome;
        }

    private void swapChromosomes()
        {
        chromosomes = new ArrayList<>();
        Collections.shuffle(choosedChromosomes);
        value = new ArrayList<>();
        System.out.println("-------------------------------------------");

        for (Integer i = 0; i < choosedChromosomes.size() / 2; i++)
            {

            Integer left = 2 * i;
            Integer right = 2 * i + 1;

            value.add(choosedChromosomes.get(left).getX());
            value.add(choosedChromosomes.get(left).getY());
            value.add(choosedChromosomes.get(right).getX());
            value.add(choosedChromosomes.get(right).getX());

            Collections.shuffle(value);

            chromosomes.add(left, new Chromosome(value.get(0), value.get(1)));
            chromosomes.add(right, new Chromosome(value.get(2), value.get(3)));

            System.out.println(chromosomes.get(left));
            System.out.println(chromosomes.get(right));

            }

        }

    public Chromosome getTheBestChromosome()
        {
        return theBestChromosome;
        }

    private void mutation()
        {

        for (Chromosome chromosome : chromosomes)
            {
            if (generator.nextDouble() < MUTATION_RADIO)
                {
                Double newX = generator.nextDouble() * MAX_VALUE_DURING_MUTATION;
                Double newY = generator.nextDouble() * MAX_VALUE_DURING_MUTATION;

                System.out.println("-------MUTACJA------------------------------------------------");

                chromosome.setX(newX.intValue());
                chromosome.setY(newY.intValue());

                System.out.println("-----" + chromosome + "-----------");
                }
            }

        }

    private void openConetionWithChart()
        {
        try
            {
            socket.connect(new InetSocketAddress("localhost", 1995), 2000);
            readFromServer = new Scanner(new BufferedInputStream(socket.getInputStream()));
            writeToServer = new PrintWriter(socket.getOutputStream(), true);
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }


        writeToServer.println("GA");
        }

    private void printChromosomes()
        {
        StringBuffer values = new StringBuffer();

        for (Chromosome chromosome : chromosomes)

            {
            values.append(chromosome.getRosenbrockFunctionEvaulate().toString());
            values.append(";");
            }

        values.deleteCharAt(values.length() - 1);


        writeToServer.println(values);
        }

    private void offConetionWithChart()
        {
        try
            {
            writeToServer.close();
            readFromServer.close();
            socket.close();
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }
        }

    }
