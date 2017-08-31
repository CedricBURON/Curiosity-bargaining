package goods;

import jade.core.AID;

import java.util.Random;


/**
 * @author Cedric Buron
 */
public class GaussianGood implements Good
{

  private final String owner;
  private String id;
  private AID ownerAgent;
  private double mean;
  private double stdDeviation;
  private long seed;
  private Random rand;


  @Override
  public void setOwnerAgent(AID ownerAgent)
  {
    this.ownerAgent = ownerAgent;
  }


  @Override
  public AID getOwnerAgent()
  {
    return ownerAgent;
  }


  @Override
  public String getId()
  {
    return id;
  }


  @Override
  public String getOwner()
  {
    return owner;
  }

  public GaussianGood(String owner, String id, AID ownerAgent, double mean, double stdDeviation, long seed)
  {
    this.owner = owner;
    this.id = id;
    this.ownerAgent = ownerAgent;
    this.mean = mean;
    this.stdDeviation = stdDeviation;
    this.seed = seed;
    rand = new Random(seed);
  }

  public String toString(){
    return owner+";"+id+";"+ mean + ";" + stdDeviation;
  }

  public GaussianGood(String s, long seed){
    this(s.split(";")[0],  s.split(";")[1], new AID( s.split(";")[1].split("-")[0], true), Double.parseDouble( s.split(";")[2]),
        Double.parseDouble(s.split(";")[3]), seed);
  }

  /**Generate randomly an evaluation price of the good.
   *
   * @return the generated price
   */
  public double generatePrice(){
    double x = rand.nextGaussian();
    double value = x*mean/2 + mean;
    if(value>=0 && value <=2*mean) {
      return value;
    }
    else return generatePrice();
  }
}
