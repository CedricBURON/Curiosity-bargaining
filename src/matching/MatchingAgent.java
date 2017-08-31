package matching;

import common.GenericAgent;
import common.Global;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import matching.behaviour.HandleRPsBehaviour;
import matching.behaviour.MatchDirectBehaviour;
import matching.behaviour.RegisterNegotiatorsBehaviour;
import matching.behaviour.SimpleAgreeBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Cedric Buron
 */
public class MatchingAgent extends GenericAgent
{
  private List<AID> sellers;
  private List<AID> purchasers;


  public int getNumberRegistered()
  {
    return numberRegistered;
  }


  public void setNumberRegistered(int numberRegistered)
  {
    this.numberRegistered = numberRegistered;
  }


  public int numberRegistered;


  public Map<AID, AID> getSellMatches()
  {
    return sellMatches;
  }


  public Map<AID, Double> getSellerPrices()
  {
    return sellerPrices;
  }


  public Map<AID, Double> getPurchaserPrices()
  {
    return purchaserPrices;
  }


  public Map<AID, AID> getPurchMatches()
  {
    return purchMatches;
  }


  private Map<AID, AID> sellMatches;
  private Map<AID, Double> sellerPrices;
  private Map<AID, Double> purchaserPrices;
  private long seed;


  public int getProtocol()
  {
    return protocol;
  }


  private int protocol;


  public int getExpectedNumberOfAgents()
  {
    return expectedNumberOfAgents;
  }


  private int expectedNumberOfAgents;
  private Map<AID, AID> purchMatches;


  public List<AID> getSellers()
  {
    return sellers;
  }


  public List<AID> getPurchasers()
  {
    return purchasers;
  }


  public long getSeed()
  {
    return seed;
  }


  @Override
  public void setup()
  {
    Object[] args = getArguments();
    if (args.length != 2) {
      throw new IllegalArgumentException("Matching agent should have two arguments. Received " + args.length);
    }
    numberRegistered =0;
    seed = (long) args[0];
    protocol = (int) args[1];
    purchasers = new ArrayList<>();
    sellers = new ArrayList<>();
    DFAgentDescription dfd = new DFAgentDescription();
    dfd.setName(getAID());
    ServiceDescription sdRegister = new ServiceDescription();
    sdRegister.setType("matching");
    sdRegister.setName(getLocalName() + "-MA");
    dfd.addServices(sdRegister);
    try {
      DFService.register(this, dfd);
    }
    catch (FIPAException fe) {
      fe.printStackTrace();
      System.err.println("Matching agent " + getAID() + "could not be registered!");
    }
    expectedNumberOfAgents = 2* Global.NB_AGENTS + (protocol % 2 == 1?Global.NB_AGENTS:0);
    sellMatches = new HashMap<>();
    purchMatches = new HashMap<>();
    sellerPrices = new HashMap<>();
    purchaserPrices = new HashMap<>();
    addBehaviour(createLifeCycleBehaviour());
    System.out.println("Matching agent ready");
  }

  /**Creates a sequential behaviour representing the life cycle of the agent, from creation to destruction
   *
   * @return a sequential behaviour representing the life cycle of the agent
   */
  private SequentialBehaviour createLifeCycleBehaviour() {
    SequentialBehaviour behaviour = new SequentialBehaviour();
    behaviour.addSubBehaviour(new RegisterNegotiatorsBehaviour());
    behaviour.addSubBehaviour(new MatchDirectBehaviour());
    if(protocol==2){
      behaviour.addSubBehaviour(new HandleRPsBehaviour());
    }
    else{
      behaviour.addSubBehaviour(new SimpleAgreeBehaviour());
    }
    return behaviour;
  }


  @Override
  protected void takeDown()
  {
    try {
      DFService.deregister(this);
    }
    catch (FIPAException e) {
      e.printStackTrace();
      System.err.println("Matching Agent " + getAID() + "could not be unregistered! ");
    }
    System.out.println("Matching Agent " + getAID() + "terminated ");
    doDelete();
  }

  /**Handle supplementary agents messages (other than negotiator)
   *
   * @param inform the message from another agent type
   */
  public void specificAgentsActions(ACLMessage inform)
  {
      throw new IllegalStateException("An unknown agent tries to register: " + inform.getSender());
  }

  public void setPurchasers(List<AID> purchasers) {
    this.purchasers = purchasers;
  }

  public void setSellers(List<AID> sellers) {
    this.sellers = sellers;
  }
}
