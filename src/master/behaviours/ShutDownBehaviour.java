
package master.behaviours;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import master.MasterAgent;

/**
 * @author Cedric Buron
 */
public class ShutDownBehaviour extends OneShotBehaviour {
  private MasterAgent getMyAgent(){
    return (MasterAgent) myAgent;
  }


  @Override
  public void action() {
    System.err.println("INFOS: Shutting down platform...");
    Codec codec = new SLCodec();
    Ontology jmo = JADEManagementOntology.getInstance();
    getMyAgent().getContentManager().registerLanguage(codec);
    getMyAgent().getContentManager().registerOntology(jmo);
    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
    msg.addReceiver(getMyAgent().getAMS());
    msg.setLanguage(codec.getName());
    msg.setOntology(jmo.getName());
    try {
      getMyAgent().getContentManager().fillContent(msg, new Action(getMyAgent().getAID(), new ShutdownPlatform()));
      getMyAgent().send(msg);
    }
    catch (Exception e) {
      System.err.println("Unable to finish the platform!");
    }
  }
}
