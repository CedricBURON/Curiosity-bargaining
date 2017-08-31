package master.behaviours;

import common.Global;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import master.MasterAgent;


/**
 * @author Cedric Buron
 */
public class HandleMetricBehaviour extends OneShotBehaviour
{

  private MasterAgent getMyAgent(){
    return (MasterAgent) myAgent;
  }

  private int goTo;

  @Override
  public void action()
  {
    //Getting result messages
    MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),MessageTemplate.or(
        MessageTemplate.MatchConversationId("Welfare"),
        MessageTemplate.MatchConversationId("NbMessages")));
    ACLMessage msg = getMyAgent().receive(template);
    if (msg != null) {
      goTo=0;
      getMyAgent().setReceivedMessages(getMyAgent().getReceivedMessages() + 1);
      if (msg.getSender().getName().contains("Purchaser")) {
        if (msg.getConversationId().equals("Welfare")) {
          getMyAgent().getPurchaserWelfare().add(Double.parseDouble(msg.getContent()));
        }
        else {
          getMyAgent().getNbMessages().add(Integer.parseInt(msg.getContent()));
        }
      }
      else if (msg.getSender().getName().contains("Seller")) {
        if (msg.getConversationId().equals("Welfare")) {
          getMyAgent().getSellerWelfare().add(Double.parseDouble(msg.getContent()));
        }
        else {
          getMyAgent().getNbMessages().add(Integer.parseInt(msg.getContent()));
        }
      }
      else throw new IllegalArgumentException("Unknown agent sent a message: " + msg);
      //Once all messages have been received
      if (getMyAgent().getReceivedMessages() == 4 * Global.NB_AGENTS) {
        goTo = 1;
      }
    }
    else {
      block();
    }
  }

  public int onEnd(){
    return goTo;
  }
}
