package matching.behaviour;

import common.Global;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import matching.MatchingAgent;


/**
 * @author Cedric Buron
 */
public class HandleRPsBehaviour extends Behaviour
{

  private int numberReplied;

  public HandleRPsBehaviour() {
    super();
    this.numberReplied = 0;
  }

  private MatchingAgent getMyAgent (){return (MatchingAgent) myAgent;}

  @Override
  public void action()
  {
    MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchConversationId("ReservePrice"),
            MessageTemplate.MatchPerformative(ACLMessage.INFORM));
    ACLMessage message = myAgent.receive(template);
    if (message != null) {
    	AID aidSender = message.getSender();
    	Double priceSender = Double.valueOf(message.getContent());
    	if(getMyAgent().getSellers().contains(aidSender))
    	{
    		AID matched = getMyAgent().getSellMatches().get(aidSender);
			Double priceMatched = getMyAgent().getPurchaserPrices().get(matched);
    		if(priceMatched != Double.NEGATIVE_INFINITY)
    		{
    			if(priceMatched >= priceSender)
    			{
        			getMyAgent().getSellerPrices().put(aidSender, priceSender);
        			numberReplied ++;
        			
        			ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
        			msg.addReceiver(aidSender);
        			msg.addReceiver(matched);
        			msg.setConversationId("Negotiate");
        			msg.setPerformative(ACLMessage.AGREE);
        			getMyAgent().send(msg);
    			}
    			else
    			{
    				getMyAgent().getPurchaserPrices().put(matched, Double.NEGATIVE_INFINITY);

        			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        			msg.addReceiver(aidSender);
        			msg.addReceiver(matched);
        			msg.setConversationId("Negotiate");
        			msg.setPerformative(ACLMessage.REQUEST);
        			getMyAgent().send(msg);
    			}
    		}
    		else
    		{
    			getMyAgent().getSellerPrices().put(aidSender, priceSender);
    		}
    	}
    	
    	else if(getMyAgent().getPurchasers().contains(aidSender))
    	{
    		AID matched = getMyAgent().getPurchMatches().get(aidSender);
			Double priceMatched = getMyAgent().getSellerPrices().get(matched);
    		if(priceMatched != Double.POSITIVE_INFINITY)
    		{
    			if(priceMatched <= priceSender)
    			{
        			getMyAgent().getPurchaserPrices().put(aidSender, priceSender);
        			numberReplied ++;
        			
        			ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
        			msg.addReceiver(aidSender);
        			msg.addReceiver(matched);
        			msg.setConversationId("Negotiate");
        			msg.setPerformative(ACLMessage.AGREE);
        			getMyAgent().send(msg);
    			}
    			else
    			{
    				getMyAgent().getSellerPrices().put(matched, Double.POSITIVE_INFINITY);

        			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        			msg.addReceiver(aidSender);
        			msg.addReceiver(matched);
        			msg.setConversationId("Negotiate");
        			msg.setPerformative(ACLMessage.REQUEST);
        			getMyAgent().send(msg);
    			}
    		}
    		else
    		{
    			getMyAgent().getPurchaserPrices().put(aidSender, priceSender);
    		}
    	}
    }
    else {
      block();
    }
  }
  @Override
  public boolean done() {
	  //System.out.println(numberReplied + " / " + Global.NB_AGENTS);
    return numberReplied == Global.NB_AGENTS;
  }
}
