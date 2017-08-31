package negotiator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class HandleAcceptBehaviour extends CyclicBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate negotiateTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("Negotiate"),
        MessageTemplate.and(MessageTemplate.MatchSender(getMyAgent().getOpponent()),
            MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL)));
    ACLMessage msg = myAgent.receive(negotiateTemplate);
    if (msg != null) {
//      assert (Double.parseDouble(msg.getContent()) == getMyAgent().getLastProposedPrice());
      getMyAgent().setProposal(Double.parseDouble(msg.getContent()));
      getMyAgent().transmitFinished();
      getMyAgent().end();
    }
    else {
      block();
    }
  }
}
