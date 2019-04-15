import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChartServer implements Runnable
    {



    @Override
    public void run()
        {
        try (ServerSocket serwer = new ServerSocket(1995))
            {
            while (true)
                {
                Socket socket = serwer.accept();
                ChartServerThread serverThread = new ChartServerThread(socket);
                new Thread(serverThread).start();
                }
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }
        }
    }
