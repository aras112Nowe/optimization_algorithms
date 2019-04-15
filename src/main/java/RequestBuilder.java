import java.util.ArrayList;

public final class RequestBuilder
    {
        StringBuffer sb;

        RequestBuilder()
        {
        }

        public String getRequest(Integer length, ArrayList<Chromosome> chromosomomes)
        {
            sb = new StringBuffer();
            sb.append("LENGTH:"+length+";");
            sb.append("CHROMOSOME:");
            for(Chromosome chromosome:chromosomomes)
            {
                sb.append(chromosome.getX().toString()+",");
                sb.append(chromosome.getY().toString()+"_");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(";");

            return sb.toString();
        }
    }

