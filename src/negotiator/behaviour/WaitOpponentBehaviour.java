package negotiator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class WaitOpponentBehaviour extends Behaviour
{
  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}


  boolean done = false;

  @Override
  public void action()
  {
    MessageTemplate template = MessageTemplate
        .and(MessageTemplate.and(MessageTemplate.MatchConversationId("Opponent"),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)),
            MessageTemplate.MatchSender(getMyAgent().getMatchmaker()));
    ACLMessage msg = myAgent.receive(template);
    if (msg != null) {
      done = true;
      getMyAgent().setOpponent(new AID(msg.getContent(), true));
      getMyAgent().addBehaviour(new HandleProposalBehaviour());
      getMyAgent().addBehaviour(new HandleAcceptBehaviour());
      getMyAgent().addBehaviour(new HandleRejectBehaviour());
      getMyAgent().endInit();
    }
    else {
      block();
    }
  }


  @Override
  public boolean done()
  {
    return done;
  }
}
