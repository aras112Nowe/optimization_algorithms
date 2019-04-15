public class Chromosome
    {
    private Integer x;
    private Integer y;
    private Integer evaulate;

    Chromosome(Integer x, Integer y)
        {
        this.x = x;
        this.y = y;
        evaulate = getRosenbrockFunctionEvaulate();
        }

    public Integer getX()
        {
        return x;
        }

    public Integer getY()
        {
        return y;
        }

    public void setX(Integer x)
        {
        this.x = x;
        evaulate = getRosenbrockFunctionEvaulate();
        }

    public void setY(Integer y)
        {
        this.y = y;
        evaulate = getRosenbrockFunctionEvaulate();
        }

    public Integer getRosenbrockFunctionEvaulate()
        {
        return 100 * ((y - (x * x)) * (y - (x * x))) + ((1 - x) * (1 - x));
        }

    @Override
    public String toString()
        {
        return "Chromosome{" +
                "x=" + x +
                ", y=" + y +
                ", evaulate=" + evaulate +
                '}';
        }
    }
