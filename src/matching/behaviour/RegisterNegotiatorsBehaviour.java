package matching.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import matching.MatchingAgent;


/**
 * @author Cedric Buron
 */
public class RegisterNegotiatorsBehaviour extends Behaviour
{
  private MatchingAgent getMyAgent (){return (MatchingAgent) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate negotiateTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("NewAgent"),
        MessageTemplate.MatchPerformative(ACLMessage.INFORM));
    ACLMessage inform = myAgent.receive(negotiateTemplate);
    if (inform != null) {
      getMyAgent().setNumberRegistered(getMyAgent().getNumberRegistered() + 1);
      if (inform.getContent().equals("seller")) {
        getMyAgent().getSellers().add(inform.getSender());
      }
      else if (inform.getContent().equals("purchaser")) {
        getMyAgent().getPurchasers().add(inform.getSender());
      }
      else {
        ((MatchingAgent) myAgent).specificAgentsActions(inform);
      }
    }
    else {
      block();
    }
  }

  @Override
  public boolean done() {
    return getMyAgent().getExpectedNumberOfAgents() == getMyAgent().getNumberRegistered();
  }
}
