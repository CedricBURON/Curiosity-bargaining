package negotiator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class HandleProposalBehaviour extends CyclicBehaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate negotiateTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("Negotiate"),
        MessageTemplate.and(MessageTemplate.MatchSender(getMyAgent().getOpponent()),
            MessageTemplate.MatchPerformative(ACLMessage.PROPOSE)));
    ACLMessage proposal = myAgent.receive(negotiateTemplate);
    if (proposal != null) {
      getMyAgent().setProposal(Double.parseDouble(proposal.getContent()));
      myAgent.addBehaviour(new ProposeBehaviour());
    }
    else {
      block();
    }
  }
}
