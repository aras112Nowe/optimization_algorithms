package pso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

//import javax.swing.SwingUtilities;

//import org.jfree.data.xy.XYDataset;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

//import ga.LineChartEx;
public class PSOAlgorithms
    {
    public Swarm swarm = new Swarm();
    private double[] pBest = new double[PSOConstants.PARTICLES]; //tablica najlepszych dopasowañ ka¿dej cz¹stki
    private Vector<Vectors> pBestLocation = new Vector<Vectors>();//najlepsza pozycja kazdej czastki
    private double gBest; //wartosc najlepszego globalnego dopasowania
    private Vectors gBestLocation; //najlepsza globalna pozycja
    private double[] fitnessValueList = new double[PSOConstants.PARTICLES]; //lista dopasowan kazdej czastki w danej literacji

    Random generator = new Random();
    String option;
    Socket socket = new Socket();
    Scanner readFromServer;
    PrintWriter writeToServer;
    ArrayList<Chromosome> chromosomes;

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


        writeToServer.println("PSO");
        }

    private void printChromosomes(Double v)
        {
        StringBuffer values = new StringBuffer();

        values.append(v.toString());
        values.append(";");

        values.deleteCharAt(values.length() - 1);


        writeToServer.println(values);
        }

    //XYDataset dataset = new XYSeriesCollection();
    //XYSeries series = new XYSeries("X", true);
    //XYSeries series2 = new XYSeries("Y", true);

    public void execute(String option)
        {
        openConnectionWithChart();
        this.option = option;
        swarm.initialize(option); //wygenerowanie roju i czastek
        updateFitnessList(); //obliczenie dopasowania
        for (int i = 0; i < PSOConstants.PARTICLES; i++)
            {
            pBest[i] = fitnessValueList[i]; //na poczatku pierwsze dopasowanie jest najlepszym dopasowaniem
            pBestLocation.add(swarm.getParticle(i).getPosition()); //na poczatku pierwsze polozenie jest najlepszym polozeniem
            }

        int t = 0;
        double w;
        double err = 9999;

        while (t < PSOConstants.SIMULATION_LENGTH)
            { //warunkiem stopu jest liczba iteracji
            // krok 1 - aktualizacja najlepszej wartoœci i pozycji lokalnej.
            for (int i = 0; i < PSOConstants.PARTICLES; i++)
                { //przy pierwszej iteracji nic nie zmieni!! dopiero przy kolejnych spr czy obecne dopasowanie jest lepsze i jesli tak to aktualizujemy tablice najlepszych dopasowan i pozycji
                if (fitnessValueList[i] < pBest[i])
                    {
                    pBest[i] = fitnessValueList[i];
                    pBestLocation.set(i, swarm.getParticle(i).getPosition());
                    }
                }

            // krok 2 - aktualizacja najlepszej wartoœci i pozycji globalnej.
            int bestParticleIndex = getMinPos(fitnessValueList); //sprawdza index czastki z minimum
            if (t == 0 || fitnessValueList[bestParticleIndex] < gBest)
                { //jezeli obecne dopasowanie czastki jest mniejsze od najlepszego globalnego to ustaw nowe globalne oraz jego pozycje
                gBest = fitnessValueList[bestParticleIndex];
                gBestLocation = swarm.getParticle(bestParticleIndex).getPosition();
                }
            //wspolczynnik inercji ruchu czastki
            w = PSOConstants.W_UPPERBOUND - (((double) t) / PSOConstants.SIMULATION_LENGTH) * (PSOConstants.W_UPPERBOUND - PSOConstants.W_LOWERBOUND);

            for (int i = 0; i < PSOConstants.PARTICLES; i++)
                {
                // krok 3 - aktualizacja pêdkoœci
                double r1 = generator.nextDouble(); //generuje dwa parametry - liczby o rozkladzie rownomiernym z przedzialu 0-1
                double r2 = generator.nextDouble();
                double px = swarm.getParticle(i).getPosition().getX();//pobieram aktualna pozycje czastki
                double py = swarm.getParticle(i).getPosition().getY(); //pobieram akutalna pozycje czastki
                double vx = swarm.getParticle(i).getVelocity().getX(); //pobieram aktualna predkosc czastki
                double vy = swarm.getParticle(i).getVelocity().getY(); //pobieram akutalna predkosc czastki
                double pBestX = pBestLocation.get(i).getX(); //pobieram najlepsza pozycje czastki
                double pBestY = pBestLocation.get(i).getY(); //pobieram najlepsza pozycje czastki
                double gBestX = gBestLocation.getX(); //pobieram najlepsza globalna pozycje
                double gBestY = gBestLocation.getY(); //pobieram najlepsza globalna pozycje


                //obliczenie nowej predkosci wedlug wzoru
                //C1,C2 to parametry okreslajace zaufanie do kierunku swojego polozenia i do polozen sasiadow
                double newVelX = (w * vx) + (r1 * PSOConstants.C1) * (pBestX - px) + (r2 * PSOConstants.C2) * (gBestX - px);
                double newVelY = (w * vy) + (r1 * PSOConstants.C1) * (pBestY - py) + (r2 * PSOConstants.C2) * (gBestY - py);
                swarm.getParticle(i).setVelocity(new Vectors(newVelX, newVelY)); //ustawienie nowej predkosci

                // aktualizacja pozycji cz¹stki wedlug wzoru x = x + newVel
                double newPosX = px + newVelX;
                double newPosY = py + newVelY;
                swarm.getParticle(i).setPosition(new Vectors(newPosX, newPosY));
                }

            //obliczanie dopasowania dla odpowiedniej funkcji
            double x = gBestLocation.getX();
            double y = gBestLocation.getY();

            if (option.equals("rPSO"))
                {
                //funkcja Rosenbrocka
                err = (Math.pow((1 - x), 2)) + (100 * (Math.pow((y - Math.pow(x, 2)), 2)));
                }
            else if (option.equals("ePSO"))
                {
                //funkcja Easom'a
                err = -Math.cos(x) * Math.cos(y) * Math.exp(-(Math.pow((x - Math.PI), 2) + Math.pow(y - Math.PI, 2)));
                }


            System.out.println("Iteriation: " + t + " - extremum is: " + err);
            printChromosomes(err);
            System.out.println("x = " + gBestLocation.getX());
            System.out.println("y = " + gBestLocation.getY());
            double var1 = gBestLocation.getX();
            double var2 = gBestLocation.getY();
            double var3 = err;
            //series2.add(var2, var3);
            //series.add(var1, var3);

            t++;
            updateFitnessList();
            }
        //((XYSeriesCollection) dataset).addSeries(series);
        //((XYSeriesCollection) dataset).addSeries(series2);
        //LineChartEx chart = new LineChartEx(dataset);

        System.out.println("\nSolution found");
        System.out.println("x = " + gBestLocation.getX());
        System.out.println("y = " + gBestLocation.getY());

        //SwingUtilities.invokeLater(() -> {
        //    chart.setVisible(true);
        //});
        }

    public void updateFitnessList()
        {
        for (int i = 0; i < PSOConstants.PARTICLES; i++)
            {
            swarm.getParticle(i).getFitness(option); //obliczenie dopasowania,ktore je zapisze w obiekcie danej czastki pod fitness
            fitnessValueList[i] = swarm.getParticle(i).fitness; //zapisanie wyzej obliczonego dopasowania do listy dopasowañ
            }
        }

    public static int getMinPos(double[] list)
        {
        int pos = 0;
        double minValue = list[0];

        for (int i = 0; i < list.length; i++)
            {
            if (list[i] < minValue)
                {
                pos = i;
                minValue = list[i];
                }
            }

        return pos;
        }
    }
