package purchaser.behaviour;

import goods.GaussianGood;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;
import negotiator.behaviour.HandleAgreeBehaviour;
import negotiator.behaviour.HandleRequestBehaviour;
import negotiator.behaviour.SendRPBehaviour;
import purchaser.Purchaser;


/**
 * @author Cedric Buron
 */
public class HandleGoodDefinition extends CyclicBehaviour
{

  private Purchaser getMyAgent(){return (Purchaser) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate goodTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("NewGood"),
        MessageTemplate.and(MessageTemplate.MatchSender(getMyAgent().getOpponent()),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM)));
    ACLMessage stringGood = getMyAgent().receive(goodTemplate);
    if (stringGood != null) {
      getMyAgent().setGood(new GaussianGood(stringGood.getContent(), getMyAgent().getSeed()));
      getMyAgent().addBehaviour(new HandleAgreeBehaviour());
		getMyAgent().addBehaviour(new HandleRequestBehaviour());
		getMyAgent().addBehaviour(new SendRPBehaviour());
//Ajoutez ici les behaviours à l'agent lorsque vous les aurez implémentés
      getMyAgent().setGamma(((Negotiator) myAgent).getGammaPerCent() * getMyAgent().getReservePrice());
      getMyAgent().removeBehaviour(this);
    }
    else {
      block();
    }

  }
}
