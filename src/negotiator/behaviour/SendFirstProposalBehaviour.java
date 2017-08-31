package negotiator.behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
public class SendFirstProposalBehaviour extends OneShotBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    Double nextProposedPrice = getMyAgent().proposedPrice(0);
    getMyAgent().setStep(getMyAgent().getStep() + 1);
    getMyAgent().setLastProposedPrice(nextProposedPrice);
    AID aid = getMyAgent().getOpponent();
    ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
    msg.addReceiver(aid);
    msg.setConversationId("Negotiate");
    msg.setContent(nextProposedPrice.toString());
    msg.setPerformative(ACLMessage.PROPOSE);
    getMyAgent().send(msg);
  }
}
