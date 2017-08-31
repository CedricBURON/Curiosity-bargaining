package purchaser;


import common.AgentType;
import goods.Good;
import jade.lang.acl.ACLMessage;
import negotiator.Negotiator;
import purchaser.behaviour.HandleGoodDefinition;


/**
 * @author Cedric Buron
 */
public class Purchaser extends Negotiator
{
  @Override
  protected String getShortName()
  {
    return "-Purchaser";
  }


  @Override
  protected String getService()
  {
    return "purchaser";
  }


  @Override
  public void firstActionOnceAgreed()
  {
    firstProposal();
  }

  @Override
  public void registerMatcher()
  {
    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
    message.addReceiver(getMatchmaker());
    message.setContent("purchaser");
    message.setPerformative(ACLMessage.INFORM);
    message.setConversationId("NewAgent");
    send(message);
  }


  @Override
  public Double computeUtility(Good good, int step, double price)
  {
    if(price==0.0 || price == Double.POSITIVE_INFINITY){
      return rejectUtility(step);
    }
    return getDynamicReservePrice() - price;
  }


  @Override
  public boolean isSeller()
  {
    return false;
  }



  @Override
  protected String deregisterErrorMessage()
  {
    return "Purchaser Agent " + getLocalName() + " could not be unregistered !";
  }


  @Override
  protected String deregisterMessage()
  {
    return "Purchaser Agent " + getLocalName() + " terminated";
  }


  public double computeDynamicReservePrice(int step, double param, double reservePrice){
    if(getType().equals(AgentType.CURIOUS)){
      return AgentType.CURIOUS.logarithmDiscount(step, param)*reservePrice;
    }
    else if(getType().equals(AgentType.SECRETIVE)){
      return 1/ AgentType.SECRETIVE.logarithmDiscount(step, param)*reservePrice;
    }
    else if (getType().equals(AgentType.UNCURIOUS)){
      return reservePrice;
    }
    else throw new IllegalStateException("type not supported: " + getType().toString());
  }
  public double rejectUtility(int step) {
    return getDynamicReservePrice() - getReservePrice();
  }

  @Override
  public Double computeGamma(Double reservePrice)
  {
    return getReservePrice()*getGammaPerCent();
  }


  @Override
  public Double proposedPrice(Integer step)
  {
    return getGamma() + (polynomial(step))*(getReservePrice() - getGamma());
  }


  @Override
  public void endInit()
  {
    addBehaviour(new HandleGoodDefinition());
  }


}
