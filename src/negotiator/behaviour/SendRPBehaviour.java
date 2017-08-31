package negotiator.behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
public class SendRPBehaviour extends OneShotBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
	  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
      msg.addReceiver(getMyAgent().getMatchmaker());
      msg.setConversationId("ReservePrice");
      msg.setContent(getMyAgent().getReservePrice().toString());
      msg.setPerformative(ACLMessage.INFORM);
      getMyAgent().send(msg);
    //Responsabilité: envoyer un message de performatif "inform", d'id de conversation "Reserve price", et dont le contenu est le reserve price au matching agent
  }
}
