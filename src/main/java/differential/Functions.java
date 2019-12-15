package differential;

public class Functions
    {
    static double RosenBrock(double x, double y)
        {
        double a = 1;
        double b = 100;
        double z1;
        double z2;
        z1 = Math.pow(a - x, 2);
        z2 = Math.pow(y - Math.pow(x, 2), 2);
        return z1 + b * z2;
        }
    }
