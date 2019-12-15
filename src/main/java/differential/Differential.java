package differential;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import pso.Chromosome;

public class Differential
    {
    int dimensions = 2;
    int n;
    double F; // [0,2]
    double CR; // [0,1]
    int space;
    int function;

    double[][] population;
    Random random;

    double[] mutant;

    double[] finalists;
    double SD = Double.POSITIVE_INFINITY;
    double STOP;

    int generation = 0;
    double minimum = Double.POSITIVE_INFINITY;

    Random generator = new Random();
    String option;
    Socket socket = new Socket();
    Scanner readFromServer;
    PrintWriter writeToServer;
    ArrayList<Chromosome> chromosomes;


    public Differential()
        {
        this.function = 1;
        this.n = 100;
        this.F = 0.2f;
        this.CR = 0.9f;
        this.space = 40;
        this.STOP = 10;
        }

    private void openConnectionWithChart()
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


        writeToServer.println("Dif");
        }

    private void printChromosomes(Double v)
        {
        StringBuffer values = new StringBuffer();

        values.append(v.toString());
        values.append(";");

        values.deleteCharAt(values.length() - 1);


        writeToServer.println(values);
        }

    public void run()
        {
        openConnectionWithChart();
        population = new double[n][dimensions];
        random = new Random();
        for (int x = 0; x < n; x++)
            {
            for (int dim = 0; dim < dimensions; dim++)
                {
                population[x][dim] = 0 + random.nextFloat() * (space); //random.nextFloat() [0,1]
                }
            }
        System.out.println(SD);
        while (SD > STOP)
            {

            generation++;

            for (int x = 0; x < population.length; x++)
                {
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 1; i < population.length; i++)
                    {
                    list.add(i);
                    }

                int finalX = x;
                list.removeIf(a -> a.equals(finalX));

                mutant = new double[dimensions];

                for (int dim = 0; dim < dimensions; dim++)
                    {
                    Collections.shuffle(list);
                    int a = list.get(0);
                    int b = list.get(1);
                    int c = list.get(2);

                    if (random.nextFloat() < this.CR)
                        {
                        mutant[dim] = a + F * (b - c);

                        }
                    else
                        {
                        mutant[dim] = population[x][dim];
                        }

                    }

                if (Functions.RosenBrock(mutant[0], mutant[1]) < Functions.RosenBrock(population[x][0], population[x][1]))
                    {
                    population[x] = mutant;
                    }

                for (double[] doubles : population)
                    {
                    if (Functions.RosenBrock(doubles[0], doubles[1]) < minimum)
                        {
                        minimum = Functions.RosenBrock(doubles[0], doubles[1]);
                        }
                    }

                finalists = new double[population.length];

                for (int j = 0; j < finalists.length; j++)
                    {
                    finalists[j] = Functions.RosenBrock(population[j][0], population[j][1]);
                    }


                SD = StandardDeviation.calculateSD(finalists);
                printChromosomes(SD);


                if(SD<1000){
                return;
                }

                System.out.println("StandardDeviation of population");
                System.out.println(SD);
                System.out.println();

                }

            System.out.println(minimum);
            System.out.println("Best minimum is " + minimum + " after generations " + generation);
            System.out.println("END");

            System.out.println(population);
            System.out.println();

            }

        }

    }
