package matching.behaviour;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import matching.MatchingAgent;

import java.util.*;


/**
 * @author Cedric Buron
 */
public class MatchDirectBehaviour extends OneShotBehaviour
{

  private MatchingAgent getMyAgent (){return (MatchingAgent) myAgent;}

  @Override
  public void action()
  {
    Random rand = new Random(getMyAgent().getSeed());
    Collections.sort(getMyAgent().getSellers(), (new Comparator<AID>()
    {
      @Override
      public int compare(AID o1, AID o2)
      {
        return o1.toString().compareTo(o2.toString());
      }
    }));
    Collections.sort(getMyAgent().getPurchasers(), (new Comparator<AID>()
    {
      @Override
      public int compare(AID o1, AID o2)
      {
        return o1.toString().compareTo(o2.toString());
      }
    }));
    List<AID> registeredPurchasers = new ArrayList<>();
    List<AID> registeredSellers = new ArrayList<>();
    while (getMyAgent().getSellers().size() != 0 && getMyAgent().getPurchasers().size() != 0) {
      //List operations
      int numSeller = rand.nextInt(getMyAgent().getSellers().size());
      int numPurchaser = rand.nextInt(getMyAgent().getPurchasers().size());
      AID seller = getMyAgent().getSellers().get(numSeller);
      AID purchaser = getMyAgent().getPurchasers().get(numPurchaser);
      registeredSellers.add(seller);
      registeredPurchasers.add(purchaser);
      getMyAgent().getSellers().remove(numSeller);
      getMyAgent().getPurchasers().remove(numPurchaser);

      //Seller message
      ACLMessage sellerMessage = new ACLMessage(ACLMessage.INFORM);
      sellerMessage.addReceiver(seller);
      sellerMessage.setPerformative(ACLMessage.INFORM);
      sellerMessage.setContent(purchaser.getName());
      sellerMessage.setConversationId("Opponent");

      //Purchaser message
      ACLMessage purchaserMessage = new ACLMessage(ACLMessage.INFORM);
      purchaserMessage.addReceiver(purchaser);
      purchaserMessage.setPerformative(ACLMessage.INFORM);
      purchaserMessage.setContent(seller.getName());
      purchaserMessage.setConversationId("Opponent");

      //put the matches in maps
      getMyAgent().getSellMatches().put(seller, purchaser);
      getMyAgent().getPurchMatches().put(purchaser, seller);
      getMyAgent().getPurchaserPrices().put(purchaser, Double.NEGATIVE_INFINITY);
      getMyAgent().getSellerPrices().put(seller, Double.POSITIVE_INFINITY);
      myAgent.send(sellerMessage);
      myAgent.send(purchaserMessage);
    }
    //set the lists back to their original values
    getMyAgent().setPurchasers(registeredPurchasers);
    getMyAgent().setSellers(registeredSellers);
  }
}
