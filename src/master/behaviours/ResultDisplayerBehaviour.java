package master.behaviours;

import common.Global;
import jade.core.behaviours.OneShotBehaviour;
import master.MasterAgent;

/**
 * @author Cedric Buron
 */
public class ResultDisplayerBehaviour extends OneShotBehaviour {

  private MasterAgent getMyAgent(){
    return (MasterAgent) myAgent;
  }

  @Override
  public void action() {
    System.out.println("\n\n*** Results ***\n\n*Sellers welfare: " + Global
            .mean(getMyAgent().getSellerWelfare()) + "\n*Purchaser welfare: " + Global
            .mean(getMyAgent().getPurchaserWelfare()) +
            "\n*Number of messages: " + Global.meanInt(getMyAgent().getNbMessages()) + "\n\n*********\n\n");
  }
}
