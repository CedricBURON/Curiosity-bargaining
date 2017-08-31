package negotiator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
public class HandleRequestBehaviour extends CyclicBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate template = MessageTemplate.and(MessageTemplate
            .and(MessageTemplate.MatchSender(getMyAgent().getMatchmaker()),
                MessageTemplate.MatchConversationId("Negotiate")),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    ACLMessage message =getMyAgent().receive(template);
    if (message != null) {

    	Double newPrice = getMyAgent().recomputeReservePrice();

  	    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(getMyAgent().getMatchmaker());
        msg.setConversationId("ReservePrice");
        msg.setContent(newPrice.toString());
        msg.setPerformative(ACLMessage.INFORM);
        getMyAgent().send(msg);
    }
    else {
      block();
    }
  }
}
