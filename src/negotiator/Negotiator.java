package negotiator;

import common.AgentType;
import common.GenericAgent;
import goods.GaussianGood;
import goods.Good;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import negotiator.behaviour.GetGeneralAgents;
import negotiator.behaviour.SendFirstProposalBehaviour;


/**
 * @author Cedric Buron
 */
public abstract class Negotiator extends GenericAgent
{
  private AgentType type;
  private AID matchmaker;
  private AID opponent;
  private int stepMax;
  private double amount;
  private double gammaPerCent;

  public AID getMaster() {
    return master;
  }

  private AID master;
  private Good good;
  private Integer step;
  private double beta;
  private double kappa;
  private Double gamma;
  private Double reservePrice;
  private double proposal;
  private double lastProposedPrice;
  private double curiosityParam;
  private long seed;
  private boolean matchMakerGotten;


  @Override
  public void setup(){
    super.setup();
    register(getService(), getShortName());
    Object[] args = getArguments();
    //Getting the arguments
    if(args.length<4){
      System.err.println("Error: too few argument (" + args.length + ") in negotiation seller agent definition.");
      throw new IllegalArgumentException("too few argument in setup of negotiation seller");
    }
    kappa = (double) args[0];
    if(kappa<0 || kappa>1){
      System.err.println("Error: kappa not in [0,1]: " + kappa);
      throw new IllegalArgumentException("wrong interval for kappa");
    }
    beta = (double)args[1];
    if(beta<0){
      System.err.println("Error: beta negative: " + beta);
      throw new IllegalArgumentException("wrong interval for beta");
    }

    //Initialise parameters
    step = 0;
    type = AgentType.fromString((String) args[2]);
    stepMax = (int) args[3];
    gammaPerCent = (double) args[4];
    seed = (long) args[5];
    curiosityParam = (double) args[6];
    amount = 0.0;

    initialiseMaster();

    //Initial first behaviour
    addBehaviour(new GetGeneralAgents());

    //"Welcome" message
    //System.out.println("Negotiation agent " + getLocalName() + " ready");
  }

  /**get master description
   *
   */
  private void initialiseMaster()
  {
    DFAgentDescription dfd = new DFAgentDescription();
    ServiceDescription sdRegister = new ServiceDescription();
    sdRegister.setType("master");
    dfd.addServices(sdRegister);
    DFAgentDescription[] masterList = new DFAgentDescription[0];
    try{
      masterList = DFService.search(this, dfd);
    }
    catch (FIPAException e) {
      e.printStackTrace();
    }
    if(masterList.length!=1){
      throw new IllegalStateException("Master agent not registered");
    }
    master = masterList[0].getName();
  }


  protected abstract String getShortName();


  protected abstract String getService();


  public double computeReservePrice(Good good){
    GaussianGood theGood =(GaussianGood) good;
    return theGood.generatePrice();
  }

  //These methods must be implemented by purchaser and seller

  /**
   * First action when oppenent is known
   */
  public abstract void firstActionOnceAgreed();

  /**
   * Register to the matching agent
   */
  public abstract void registerMatcher();

  /**Compute the utility of the agent
   *
   * @param good the good being negotiated
   * @param step Step of the end of the negotiation
   * @param price agreed price, 0 if negotiation has failed
   * @return the utility of the agent
   */
  public abstract Double computeUtility(Good good, int step, double price);

  /**is a seller
   *
   * @return true iff the agent is a seller
   */
  public abstract boolean isSeller();

  /**Compute gamma, a parameter of the strategy
   *
   * @param reservePrice the reserve price of the agent over the good
   * @return gamma, a parameter of the strategy
   */
  public abstract Double computeGamma(Double reservePrice);

  /**
   * End initialization
   */
  public abstract void endInit();

  /**Computes the proposed price at a step of the negotiation
   *
   * @param step the step of the negotiation
   * @return the proposed price at the given step
   */
  public abstract Double proposedPrice(Integer step);

  /**The error message when deregistering fails
   *
   * @return the error message when deregistering fails
   */
  protected abstract String deregisterErrorMessage();

  /**The  message when deregistering successes
   *
   * @return the message when deregistering successes
   */
  protected abstract String deregisterMessage();

  /**Compute the dynamically computed reserve price
   *
   * @param step step of the negotiation
   * @param curiosityParam a parameter measuring curiosity
   * @param reservePrice The reserve price of the agent over the good
   * @return the dynamically computed reserve price
   */
  public abstract double computeDynamicReservePrice(int step, double curiosityParam, double reservePrice);

  /**The utility gotten if the negotiation fails
   *
   * @param step step at which the negotiation has failed
   * @return The utility gotten at the given step
   */
  protected abstract double rejectUtility(int step);


  //setters and getters


  public double getGamma()
  {
    return gamma;
  }


  public double getBeta()
  {
    return beta;
  }


  public double getKappa()
  {

    return kappa;
  }


  public AgentType getType()
  {
    return type;
  }



  public AID getOpponent()
  {
    return opponent;
  }


  public int getStepMax()
  {
    return stepMax;
  }


  public void setAmount(double amount)
  {
    this.amount = amount;
  }

  public double getAmount()
  {
    return amount;
  }


  public AID getMatchmaker()
  {
    return matchmaker;
  }


  public void setOpponent(AID opponent)
  {
    this.opponent = opponent;
  }

  public long getSeed()
  {
    return seed;
  }

  public double getGammaPerCent()
  {
    return gammaPerCent;
  }
  public void setMatchmaker(AID matchmaker) {
    this.matchmaker = matchmaker;
  }


  public void setGamma(Double gamma)
  {
    this.gamma = gamma;
  }


  public boolean isMatchMakerGotten() {
    return matchMakerGotten;
  }


  public void setMatchMakerGotten(boolean matchMakerGotten) {
    this.matchMakerGotten = matchMakerGotten;
  }


  public Good getGood()
  {
    return good;
  }


  public void setGood(Good good)
  {
    this.good = good;
  }


  public Integer getStep()
  {
    return step;
  }

  public void setStep(Integer step)
  {
    this.step = step;
  }

  //Lazy initialisation
  public Double getReservePrice(){
    if(reservePrice==null){
      reservePrice = computeReservePrice(good);
    }
    return reservePrice;
  }

  public Double recomputeReservePrice(){
    reservePrice = computeReservePrice(good);
    gamma = computeGamma(reservePrice);
//    System.out.println(getMyAgent() + " " + gamma + " " + reservePrice + " " + ((Negotiator)getMyAgent()).getGammaPerCent());
    return reservePrice;
  }


  public Double getDynamicReservePrice()
  {
    if(reservePrice==null){
      reservePrice = computeReservePrice(good);
    }
    return computeDynamicReservePrice(step, curiosityParam,reservePrice);
  }


  public void setProposal(double proposal)
  {
    this.proposal = proposal;
  }


  public Double getProposal()
  {
    return proposal;
  }


  public double getLastProposedPrice()
  {
    return lastProposedPrice;
  }


  public void setLastProposedPrice(double lastProposedPrice)
  {
    this.lastProposedPrice = lastProposedPrice;
  }

  /**Polynomial function - used to compute the proposals
   *
   * @param step
   * @return
   */
  public double polynomial(int step){
    return getKappa() + (1.0-getKappa())*Math.pow(((double)step/(double)getStepMax()),1.0/getBeta());
  }

  /**Send a reject message
   *
   */
  public void reject()
  {
    ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
    msg.setConversationId("Negotiate");
    msg.setContent(getGood().getId());
    msg.setPerformative(ACLMessage.REJECT_PROPOSAL);
    AID aid = getOpponent();
    msg.addReceiver(aid);
    send(msg);
  }

  /**
   * Transmit statistics to master
   */
  public void transmitFinished()
  {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setConversationId("Welfare");
    msg.setPerformative(ACLMessage.INFORM);
    msg.setContent(computeUtility(getGood(), getStep(),
        getAmount()).toString());
    msg.addReceiver(getMaster());
    send(msg);
    msg.setConversationId("NbMessages");
    msg.setContent(getStep().toString());
    send(msg);
  }

  /**true iff price1 is better for the agent than price2
   *
   * @param price1 a price
   * @param price2 another price
   * @return agent prefers price1 to price2
   */
  public boolean isBetter(double price1, double price2)
  {
    if (isSeller()) {
      return price1 > price2;
    }
    else {
      return price2 > price1;
    }
  }

  /**prepare the first proposal of the agent
   *
   */
  protected void firstProposal()
  {
    addBehaviour(new SendFirstProposalBehaviour());
  }

  /**terminate the agent
   *
   */
  public void end(){
    doDelete();
  }

  @Override
  protected void takeDown()
  {
    try {
      DFService.deregister(this);
    }
    catch (FIPAException e) {
      e.printStackTrace();
      System.err.println(deregisterErrorMessage());
    }
    //System.out.println(deregisterMessage());
    super.takeDown();
  }

}