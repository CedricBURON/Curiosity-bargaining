package common;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Cedric Buron
 */
public abstract class GenericAgent extends Agent
{
  /**
   * Registers the service of the agent
   * @param type The service to register
   * @param shortName short name of the service
   */
  public void register(String type, String shortName)
  {
    DFAgentDescription dfd = new DFAgentDescription();
    dfd.setName(getAID());
    ServiceDescription sdRegister = new ServiceDescription();
    sdRegister.setType(type);
    sdRegister.setName(getLocalName() + shortName);
    dfd.addServices(sdRegister);
    try {
      DFService.register(this, dfd);
    }
    catch (FIPAException fe) {
      fe.printStackTrace();
      System.err.println(type + " Agent " + getAID() + " could not be registered !");
    }
  }
}
