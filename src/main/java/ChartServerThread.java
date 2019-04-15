

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

public class ChartServerThread implements Runnable
    {
    Scanner readFromClient;
    PrintWriter writeToClient;
    Socket socket;
    XYChart chart;
    List<String> list;
    ArrayList<Double> value;
    double[] tabY = {0.0};
    double[] tabX = {0.0};
    JFrame swingWrapper;
    private static Integer SLEEP_TIME = 100;
    String algorithm;


    ChartServerThread(Socket socket) throws IOException
        {
        this.socket = socket;
        list = new ArrayList<>();
        value = new ArrayList<>();
        init();
        }


    @Override
    public void run()
        {

        String napis = readFromClient.nextLine();

        if (napis.equals("GA"))
            {
            algorithm = "Genetic Algorithm";
            }
        if (napis.equals("SA"))
            {
            algorithm = "Simulated Annealing";
            }

        chart = QuickChart.getChart("Sample Chart", "Chromosoms", "Rosenbrock function evaulate", algorithm, tabX, tabY);
        swingWrapper = new SwingWrapper(chart).displayChart();


        while (readFromClient.hasNextLine())
            {
            napis = readFromClient.nextLine();
            list = Arrays.asList(napis.split(";"));

            for (String stringValue : list)
                {

                value.add(Double.valueOf(stringValue));

                }

            Integer i = 0;
            tabY = new double[value.size()];
            tabX = new double[value.size()];

            for (Double curretDouble : value)
                {
                tabY[i] = curretDouble;
                tabX[i] = i;
                i++;

                }

            chart.updateXYSeries(algorithm, tabX, tabY, tabX);
            swingWrapper.repaint();

            // Create Chart
            //chart = QuickChart.getChart("Sample Chart", "Chromosom", "Rosenbrock function evaulate", "y(x)", tabX, tabY);

            // Show it
            // swingWrapper = new SwingWrapper(chart).displayChart();


            try
                {
                Thread.sleep(SLEEP_TIME);
                }
            catch (InterruptedException e)
                {
                e.printStackTrace();
                }


            }
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
    }
