import java.util.*;

public final class ParseRequest
    {
    private List<String> lines;
    private List<String> chromosomesString;
    private Integer length;
    private String rawChromosomesFromRequest;
    private ArrayList<Chromosome> chromosomes;

    ParseRequest()
        {
        chromosomes = new ArrayList<>();
        }

    public void parse(String request)
        {
        chromosomes = new ArrayList<>();
        lines = Arrays.asList(request.split(";"));
        getInfo();
        }

    enum RequestNumber
        {
            LENGTH(0), CHROMOSOM(1);

        Integer number;

        RequestNumber(Integer i)
            {
            number = i;
            }

        Integer getNumber()
            {
            return number;
            }

        }

    public ArrayList<Chromosome> getChromosomes()
        {
        return chromosomes;
        }

    private void getInfo()
        {
        length = Integer.valueOf(lines.get(RequestNumber.LENGTH.getNumber()).split(":")[1].trim());
        rawChromosomesFromRequest = lines.get(RequestNumber.CHROMOSOM.getNumber()).split(":")[1].trim();

        chromosomesString = Arrays.asList(rawChromosomesFromRequest.split("_"));

        for (String chromosom : chromosomesString)
            {
            chromosomes.add(new Chromosome(Integer.valueOf(chromosom.split(",")[0]), Integer.valueOf(chromosom.split(",")[1])));
            }

        }

    }