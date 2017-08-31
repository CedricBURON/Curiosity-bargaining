package master;

import common.GenericAgent;
import common.Parameter;
import jade.core.behaviours.FSMBehaviour;
import jade.wrapper.AgentContainer;
import jade.wrapper.ContainerController;
import master.behaviours.HandleMetricBehaviour;
import master.behaviours.LaunchExperimentsBehaviour;
import master.behaviours.ResultDisplayerBehaviour;
import master.behaviours.ShutDownBehaviour;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Cedric Buron
 */
public class MasterAgent extends GenericAgent
{
  private long seed;
  private Parameter param;
  private int protocol;
  private ContainerController container;
  private int receivedMessages;
  private List<Double> SellerWelfare;
  private List<Double> PurchaserWelfare;
  private List<Integer> NbMessages;

  public long getSeed() {
    return seed;
  }

  @Override
  public void setup(){
    super.setup();
    System.out.println("Master agent " + getLocalName() + " ready");
    Object[] args = getArguments();
    param = (Parameter) args[0];
    seed = param.getSeed();
    protocol = param.getProtocol();
    PurchaserWelfare = new ArrayList<>();
    SellerWelfare = new ArrayList<>();
    NbMessages = new ArrayList<>();
    receivedMessages = 0;
    register();
    addBehaviour(createCycleLifeBehaviour());

    addBehaviour(new LaunchExperimentsBehaviour());
    System.out.println("Master agent " + getLocalName() + " ready");
  }

  /**Generates the overall behaviour of the agent
   *
   * @return the behaviour
   */
  private FSMBehaviour createCycleLifeBehaviour() {
    FSMBehaviour behaviour = new FSMBehaviour();
    behaviour.registerFirstState(new HandleMetricBehaviour(), "inProgress");
    behaviour.registerState(new ResultDisplayerBehaviour(), "finished");
    behaviour.registerLastState(new ShutDownBehaviour(), "shutdown");

    behaviour.registerTransition("inProgress", "inProgress", 0);
    behaviour.registerTransition("inProgress", "finished", 1);
    behaviour.registerDefaultTransition("finished", "shutdown");
    return behaviour;
  }

   /**Register the agent to the DF
   *
   */
  private void register() {
    super.register("master", "-Mst");
  }

  public List<Integer> getNbMessages()
  {
    return NbMessages;
  }


  public List<Double> getPurchaserWelfare()
  {
    return PurchaserWelfare;
  }


  public List<Double> getSellerWelfare()
  {
    return SellerWelfare;
  }


  public int getReceivedMessages()
  {
    return receivedMessages;
  }


  public void setReceivedMessages(int receivedMessages)
  {
    this.receivedMessages = receivedMessages;
  }

  public ContainerController getContainer() {
    return container;
  }

  public void setContainer(AgentContainer container) {
    this.container = container;
  }

  public int getProtocol() {
    return protocol;
  }


  public Parameter getParam() {
    return param;
  }
}
