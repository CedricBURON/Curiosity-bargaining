package negotiator.behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class AcceptBehaviour extends OneShotBehaviour
{
  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    getMyAgent().setAmount(getMyAgent().getProposal());
    AID aid = getMyAgent().getOpponent();
    ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
    msg.addReceiver(aid);
    msg.setConversationId("Negotiate");
    msg.setContent(getMyAgent().getProposal().toString());
    msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
    getMyAgent().send(msg);
    getMyAgent().transmitFinished();
    getMyAgent().end();
  }
}
