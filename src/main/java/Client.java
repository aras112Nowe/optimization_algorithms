import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable
    {

    Socket socket;
    Scanner readFromServer;
    PrintWriter writeToServer;
    String name;
    RequestBuilder requestBuilder;

    Client(String name)
        {
        this.name = name;
        socket = new Socket();
        requestBuilder = new RequestBuilder();
        }

    @Override
    public void run()
        {
        try
            {
            init();
            sendPopulation();
            }
        finally
            {
            offClient();
            }
        }

    private void init()
        {
        try
            {
            socket.connect(new InetSocketAddress("localhost", 1996), 2000);
            readFromServer = new Scanner(new BufferedInputStream(socket.getInputStream()));
            writeToServer = new PrintWriter(socket.getOutputStream(), true);
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }
        }

    private void sendPopulation()
        {
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome(4, 4));
        chromosomes.add(new Chromosome(3, 1));
        chromosomes.add(new Chromosome(6, 2));
        chromosomes.add(new Chromosome(1, 2));
        chromosomes.add(new Chromosome(3, 7));
        chromosomes.add(new Chromosome(10, 2));
        writeToServer.println(requestBuilder.getRequest(chromosomes.size(), chromosomes));
        }

    private void offClient()
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
