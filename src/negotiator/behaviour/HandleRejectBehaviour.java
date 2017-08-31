package negotiator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class HandleRejectBehaviour extends CyclicBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}


  @Override
  public void action()
  {
    MessageTemplate negotiateTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("Negotiate"),
        MessageTemplate.and(MessageTemplate.MatchSender(getMyAgent().getOpponent()),
            MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL)));
    ACLMessage reject = myAgent.receive(negotiateTemplate);
    if (reject != null) {
      getMyAgent().transmitFinished();
      getMyAgent().end();
    }
    else {
      block();
    }
  }
}
