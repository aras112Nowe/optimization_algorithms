import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SimulatedAnnealing
    {

    private ArrayList<Chromosome> chromosomes;
    private ArrayList<Chromosome> newChromosomes;
    private ArrayList<Integer> purposeResult;
    private final Integer MAX_CHANGE = 5;
    private final Integer START_TEMP = 300;
    private final Double TEMP_COOLED = 0.9;
    private Random generator;
    private double temp;
    Socket socket;
    Scanner readFromServer;
    PrintWriter writeToServer;


    public SimulatedAnnealing(ArrayList<Chromosome> chromosomes)
        {
        this.chromosomes = chromosomes;
        generator = new Random();
        temp = START_TEMP;
        socket = new Socket();
        }

    public void exec()
        {
        openConetionWithChart();

        while (temp > 5)
            {
            printChromosomes();
            calculatePurpose();
            changeChromosomeValue();
            cooledDown();
            }
        offConetionWithChart();
        }

    private void calculatePurpose()
        {
        //purposeResult = new ArrayList<>();

        for (Chromosome chromosome : chromosomes)
            {
            //purposeResult.add(chromosome.getRosenbrockFunctionEvaulate());
            System.out.println(chromosome);
            }
        }

    private void changeChromosomeValue()
        {
        Integer position = 0;
        Chromosome newChromoseome;
        newChromosomes = new ArrayList<>(chromosomes);

        for (Chromosome chromosome : chromosomes)
            {
            newChromoseome = new Chromosome(chromosome.getX() + randValueForChromosome(), chromosome.getY() + randValueForChromosome());

            if (newChromoseomeIsBetter(newChromoseome, chromosome))
                {
                newChromosomes.set(position, newChromoseome);
                System.out.println("better and change " + newChromoseome);
                }
            else
                {
                Integer difference = -newChromoseome.getRosenbrockFunctionEvaulate() + chromosome.getRosenbrockFunctionEvaulate();
                Double ratio = Double.valueOf(difference / temp);
                Double exp = Math.exp(ratio);

                if (chanceForExchange(exp))
                    {
                    newChromosomes.set(position, newChromoseome);
                    System.out.println("NO NO NO ---- no better and change " + newChromoseome);
                    }
                }
            position++;
            }
        chromosomes = newChromosomes;
        }

    private boolean chanceForExchange(Double exp)
        {
        return generator.nextDouble() < exp;
        }

    private boolean newChromoseomeIsBetter(Chromosome newChromoseome, Chromosome chromosome)
        {
        return newChromoseome.getRosenbrockFunctionEvaulate() < chromosome.getRosenbrockFunctionEvaulate();
        }

    private Integer randValueForChromosome()
        {
        Double value = generator.nextDouble();
        value *= MAX_CHANGE;

        if (generator.nextDouble() > 0.5)
            {
            value *= -1;
            }

        return value.intValue();
        }

    private void cooledDown()
        {
        temp = temp * TEMP_COOLED;
        }

    public Chromosome getTheBestChromosome()
        {
        Chromosome theBestChromosome = chromosomes.get(0);
        Integer evaulate;

        evaulate = chromosomes.get(0).getRosenbrockFunctionEvaulate();


        for (Chromosome chromosome : chromosomes)
            {
            if (evaulate > chromosome.getRosenbrockFunctionEvaulate())
                {
                evaulate = chromosome.getRosenbrockFunctionEvaulate();
                theBestChromosome = chromosome;
                }
            }


        return theBestChromosome;
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

        writeToServer.println("SA");
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
