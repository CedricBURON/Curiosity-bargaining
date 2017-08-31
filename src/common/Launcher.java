package common;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.domain.DFService;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import master.MasterAgent;


/**
 * @author Cedric Buron
 */
public class Launcher
{


  /**main function - Launch the JADE Platform and master agent
   *
   * @param args
   */
  public static void main(String[] args){
    StringBuilder builder = new StringBuilder(args[0] + " ");
    for(int i = 1; i<args.length; i++){
      builder.append(args[i] + " ");
    }
    System.out.println(builder.toString());
    if(args.length>8 || args.length<7){
      System.out.println("usage: java common.Launcher seed proportion_of_secretive_sellers proportion_of_curious_sellers proportion_of_secretive_purchass proportion_of_curious_purchasers is_modified_protocol port");
    }
    else{
      //Get parameters
      int nbXP = Integer.parseInt(args[0]);
      double propSecretiveSeller = Double.parseDouble(args[1]);
      double propSecretivePurchaser = Double.parseDouble(args[2]);
      double propCuriousSeller = Double.parseDouble(args[3]);
      double propCuriousPurchaser = Double.parseDouble(args[4]);
      int protocol = Integer.parseInt(args[5]);
      Global.PROTOCOL = protocol;
      Global.PORT = Integer.parseInt(args[6]);

      //Create parameter object
      Parameter param = new Parameter(protocol, propCuriousSeller, propSecretiveSeller, propCuriousPurchaser,
              propSecretivePurchaser, nbXP);

      //Launch JADE
      jade.core.Runtime runtime = jade.core.Runtime.instance();
      Profile profile = new ProfileImpl(null, Global.PORT, null);
      profile.setParameter(DFService.DF_SEARCH_TIMEOUT_KEY, "180000");
      ContainerController mainContainer = runtime.createMainContainer(profile);

      //Create master agent
      try {
        AgentController mainController = mainContainer.createNewAgent("main", MasterAgent.class.getName(), new Object[] {param});
        mainController.start();
      }
      catch (StaleProxyException e) {
        e.printStackTrace();
      }
    }
  }
}
