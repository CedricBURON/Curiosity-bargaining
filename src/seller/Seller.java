package seller;

import common.AgentType;
import goods.GaussianGood;
import goods.Good;
import jade.lang.acl.ACLMessage;
import negotiator.Negotiator;
import negotiator.behaviour.HandleAgreeBehaviour;
import negotiator.behaviour.HandleRequestBehaviour;
import negotiator.behaviour.SendRPBehaviour;
import seller.behaviour.SendGoodInfoBehaviour;

import java.util.Random;


/**
 * @author Cedric Buron
 */
public class Seller extends Negotiator
{
  @Override
  public void setup(){
    super.setup();
    Random rand = new Random(getSeed());
    GaussianGood good = new GaussianGood(getLocalName(),getAID().getName()+"-01",getAID(),rand.nextDouble()*100.0, rand.nextDouble()*2.0+3.0, getSeed());
    setGood(good);
    setGamma(getReservePrice()*(1.0+getGammaPerCent()));
  }


  @Override
  protected String getShortName()
  {
    return "seller";
  }


  @Override
  protected String getService()
  {
    return "Seller";
  }


  @Override
  public Double computeUtility(Good good, int step, double price)
  {
    if(price == 0.0){
      return rejectUtility(step);
    }
    return price - getDynamicReservePrice();
  }


  @Override
  public boolean isSeller()
  {
    return true;
  }


  @Override
  protected String deregisterErrorMessage()
  {
    return "Seller Agent " + getLocalName() + " could not be unregistered !";
  }


  @Override
  protected String deregisterMessage()
  {
    return "Seller Agent " + getLocalName() + " terminated";
  }


  @Override
  public void registerMatcher()
  {
    ACLMessage message = new ACLMessage(ACLMessage.INFORM);
    message.addReceiver(getMatchmaker());
    message.setContent("seller");
    message.setPerformative(ACLMessage.INFORM);
    message.setConversationId("NewAgent");
    send(message);
  }


  public double computeDynamicReservePrice(int step, double param, double reservePrice){
    switch (getType()){
      case SECRETIVE:
        return AgentType.SECRETIVE.logarithmDiscount(step, param)*reservePrice;
      case CURIOUS:
        return 1/ AgentType.CURIOUS.logarithmDiscount(step, param)*reservePrice;
      case UNCURIOUS:
        return reservePrice;
      default:
        throw new IllegalStateException("type not supported: " + getType().toString());
    }
  }

  public double rejectUtility(int step) {
    return getReservePrice() - getDynamicReservePrice();
  }


  @Override
  public Double computeGamma(Double reservePrice)
  {
  return getReservePrice()*(1.0+getGammaPerCent());
  }


  @Override
  public Double proposedPrice(Integer step) {
    return getReservePrice() + (1.0-polynomial(step))*(getGamma() - getReservePrice());
  }

  public void endInit()
  {
    addBehaviour(new SendGoodInfoBehaviour());
    addBehaviour(new HandleRequestBehaviour());
    addBehaviour(new SendRPBehaviour());
//Ajoutez ici les behaviours à l'agent lorsque vous les aurez implémentés
  }

  @Override
  public void firstActionOnceAgreed()
  {
    //do nothing
  }


}