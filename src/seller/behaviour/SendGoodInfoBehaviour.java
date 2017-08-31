package seller.behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import negotiator.behaviour.HandleAgreeBehaviour;
import negotiator.behaviour.HandleRequestBehaviour;
import negotiator.behaviour.SendRPBehaviour;
import seller.Seller;


/**
 * @author Cedric Buron
 */
public class SendGoodInfoBehaviour extends OneShotBehaviour
{

  private Seller getMyAgent(){return (Seller) myAgent;}

  @Override
  public void action()
  {
    getMyAgent().setLastProposedPrice(Double.POSITIVE_INFINITY);
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.addReceiver(getMyAgent().getOpponent());
    msg.setConversationId("NewGood");
    msg.setContent(getMyAgent().getGood().toString());
    msg.setPerformative(ACLMessage.INFORM);
    getMyAgent().send(msg);
    getMyAgent().addBehaviour(new HandleAgreeBehaviour());
    getMyAgent().addBehaviour(new HandleRequestBehaviour());
    getMyAgent().addBehaviour(new SendRPBehaviour());
  }
}
