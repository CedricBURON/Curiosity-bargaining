package matching.behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import matching.MatchingAgent;

/**
 * @author Cedric Buron
 */
public class SimpleAgreeBehaviour extends OneShotBehaviour {

  private MatchingAgent getMyAgent (){return (MatchingAgent) myAgent;}

  @Override
  public void action() {
    for (AID a : getMyAgent().getPurchasers()) {
      ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
      agree.addReceiver(a);
      agree.setPerformative(ACLMessage.AGREE);
      agree.setConversationId("Negotiate");
      getMyAgent().send(agree);
    }
    for (AID a : getMyAgent().getSellers()) {
      ACLMessage agree = new ACLMessage(ACLMessage.AGREE);
      agree.addReceiver(a);
      agree.setPerformative(ACLMessage.AGREE);
      agree.setConversationId("Negotiate");
      getMyAgent().send(agree);
    }

  }
}
