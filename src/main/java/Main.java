import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
    {

    public static void main(String[] args)
        {
        new Thread(new ChartServer()).start();
        Client client1 = new Client("klient");
        new Thread(client1).start();

        serverStart();
        }

    static void serverStart()
        {
        try (ServerSocket serwer = new ServerSocket(1996))
            {
            while (true)
                {
                Socket socket = serwer.accept();
                ServerThread serverThread = new ServerThread(socket);
                new Thread(serverThread).start();
                }
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }



        }

    }

