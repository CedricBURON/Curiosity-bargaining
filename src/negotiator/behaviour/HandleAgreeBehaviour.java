package negotiator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
public class HandleAgreeBehaviour extends CyclicBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate template = MessageTemplate.and(MessageTemplate
            .and(MessageTemplate.MatchSender(getMyAgent().getMatchmaker()),
                MessageTemplate.MatchConversationId(
                    "Negotiate")),
        MessageTemplate.MatchPerformative(ACLMessage.AGREE));
    ACLMessage message = getMyAgent().receive(template);
    if (message != null) {
//    	System.out.println("foobar");
      getMyAgent().firstActionOnceAgreed();
    }
    else {
      block();
    }
  }
}
