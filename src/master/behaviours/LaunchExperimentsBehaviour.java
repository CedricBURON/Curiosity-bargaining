package master.behaviours;

import common.AgentType;
import common.Global;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import master.MasterAgent;
import matching.MatchingAgent;
import purchaser.Purchaser;
import seller.Seller;

import java.util.Random;

/**@author Cedric Buron*/

public class LaunchExperimentsBehaviour extends OneShotBehaviour {

  private MasterAgent getMyAgent(){
    return (MasterAgent) myAgent;
  }

  @Override
  public void action() {
    AgentController matcherController;
    try {
      Random rand = new Random(getMyAgent().getSeed());
      jade.core.Runtime runtime = jade.core.Runtime.instance();
      Profile profile = new ProfileImpl(null, Global.PORT, null);
      getMyAgent().setContainer(runtime.createAgentContainer(profile));

      if(getMyAgent().getProtocol()>0){
        matcherController = getMyAgent().getContainer().createNewAgent("Matchmaker",
                MatchingAgent.class.getName(), new Object[] { getMyAgent().getSeed(), getMyAgent().getProtocol() });
        matcherController.start();
      }
      else {
        matcherController = getMyAgent().getContainer().createNewAgent("Matchmaker",
                MatchingAgent.class.getName(), new Object[] { getMyAgent().getSeed(), getMyAgent().getProtocol() });
        matcherController.start();
      }
      Global.lock = false;
      //create sellers and purchasers
      for (int i = 0; i < Global.NB_AGENTS; i++) {
        //purchaser
        Object[] purchaserParameters = new Object[8];
        purchaserParameters[0] = rand.nextDouble();
        purchaserParameters[1] = Math.abs(rand.nextDouble())*100;
        if (i < Global.NB_AGENTS * getMyAgent().getParam().getPurchaserSecretiveProportion()) {
          purchaserParameters[2] = AgentType.SECRETIVE.toString();
        }
        else if (i < Global.NB_AGENTS * (getMyAgent().getParam().getPurchaserSecretiveProportion() + getMyAgent().getParam().getSellerCuriousProportion())) {
          purchaserParameters[2] = AgentType.CURIOUS.toString();
        }
        else {
          purchaserParameters[2] = AgentType.UNCURIOUS.toString();
        }
        purchaserParameters[3] = rand.nextInt(1000);
        purchaserParameters[4] = rand.nextDouble();
        purchaserParameters[5] = rand.nextLong();
        purchaserParameters[6] = rand.nextDouble() * 2.0;
        purchaserParameters[7] = getMyAgent().getProtocol()%2==1;
        Object[] sellerParameters = new Object[8];
        //seller
        sellerParameters[0] = rand.nextDouble();
        sellerParameters[1] = Math.abs(rand.nextDouble());
        if (i < Global.NB_AGENTS * getMyAgent().getParam().getSellerSecretiveProportion()) {
          sellerParameters[2] = AgentType.SECRETIVE.toString();
        }
        else if (i < Global.NB_AGENTS * (getMyAgent().getParam().getSellerSecretiveProportion() + getMyAgent().getParam().getSellerCuriousProportion())) {
          sellerParameters[2] = AgentType.CURIOUS.toString();
        }
        else {
          sellerParameters[2] = AgentType.UNCURIOUS.toString();
        }
        sellerParameters[3] = rand.nextInt(1000);
        sellerParameters[4] = rand.nextDouble();
        sellerParameters[5] = rand.nextLong();
        sellerParameters[6] = rand.nextDouble() * 2.0;
        sellerParameters[7] = getMyAgent().getProtocol()%2==1;
        AgentController sellerController;
        AgentController purchaserController;
        sellerController = getMyAgent().getContainer()
                .createNewAgent("Seller" + String.format("%05d", i), Seller.class.getName(), sellerParameters);
        purchaserController = getMyAgent().getContainer().createNewAgent(
                "Purchaser" + String.format("%05d", i),
                Purchaser.class.getName(), purchaserParameters);
        sellerController.start();
        purchaserController.start();
      }
    }
    catch (StaleProxyException e) {
      e.printStackTrace();
    }

  }

}
