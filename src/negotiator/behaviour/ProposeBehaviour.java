package negotiator.behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class ProposeBehaviour extends OneShotBehaviour
{
  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    Double nextProposedPrice = getMyAgent().proposedPrice(getMyAgent().getStep());
    if (getMyAgent().isBetter(getMyAgent().getDynamicReservePrice(), nextProposedPrice) || getMyAgent()
        .isBetter(nextProposedPrice,
            getMyAgent().getLastProposedPrice())) {
      getMyAgent().addBehaviour(new RejectBehaviour());
    }
    else if (getMyAgent().isBetter(getMyAgent().getProposal(), nextProposedPrice)) {
      getMyAgent().addBehaviour(new AcceptBehaviour());
    }
    else {
      getMyAgent().setLastProposedPrice(nextProposedPrice);
      getMyAgent().setStep(getMyAgent().getStep() + 1);
      AID aid = getMyAgent().getOpponent();
      ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
      msg.addReceiver(aid);
      msg.setConversationId("Negotiate");
      msg.setContent(nextProposedPrice.toString());
      msg.setPerformative(ACLMessage.PROPOSE);
      getMyAgent().send(msg);
    }
  }
}
