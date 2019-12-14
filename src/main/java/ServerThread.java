import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import pso.PSOAlgorithms;


public class ServerThread implements Runnable
    {
    Scanner readFromClient;
    PrintWriter writeToClient;
    Socket socket;
    ParseRequest parseRequest;
    ArrayList<Chromosome> list;
    GeneticAlgorithm geneticAlgorithm;
    SimulatedAnnealing simulatedAnnealing;

    ServerThread(Socket socket) throws IOException
        {
        this.socket = socket;
        parseRequest = new ParseRequest();
        }


    @Override
    public void run()
        {
        init();
        checkClientRequest();
        offThread();
        }

    private void init()
        {
        try
            {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            readFromClient = new Scanner(inputStream);
            writeToClient = new PrintWriter(outputStream, true);
            }
        catch (IOException e)
            {
            System.out.println(e.getMessage());
            }
        }

    private void checkClientRequest()
        {
        while (true)
            {
            while (readFromClient.hasNextLine())
                {
                String napis = readFromClient.nextLine();
                parseRequest.parse(napis);
                list = parseRequest.getChromosomes();
                simulatedAnnealing=new SimulatedAnnealing(list);
                simulatedAnnealing.exec();
                geneticAlgorithm = new GeneticAlgorithm(list);
                geneticAlgorithm.exec();
                new PSOAlgorithms().execute("rPSO");
                System.out.println("-----WYNIK----------------Genetic Algorithm:");
                System.out.println(geneticAlgorithm.getTheBestChromosome());
                System.out.println("-----WYNIK----------------Simulated Annealing:");
                System.out.println(simulatedAnnealing.getTheBestChromosome());
                }
            }
        }

    private void offThread()
        {
        readFromClient.close();
        writeToClient.close();
        try
            {
            socket.close();
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }
        }
    }


