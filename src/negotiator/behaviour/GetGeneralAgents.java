package negotiator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import negotiator.Negotiator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Cedric Buron
 */
public class GetGeneralAgents extends Behaviour
{

  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    List<AID> matchers = new ArrayList<AID>();
    DFAgentDescription template;
    ServiceDescription sdSearch;
    if (!getMyAgent().isMatchMakerGotten()) {
      template = new DFAgentDescription();
      sdSearch = new ServiceDescription();
      sdSearch.setType("matching");
      template.addServices(sdSearch);
      try {
        DFAgentDescription[] result = DFService.search(getMyAgent(), template);
        matchers = new ArrayList<AID>();
        for (DFAgentDescription r : result) {
          matchers.add(r.getName());
        }
      }
      catch (FIPAException e) {
        e.printStackTrace();
        System.err.println("Negotiation agent " + getMyAgent().getAID() + " could not get matching agent");
      }
    }
    if (matchers.size() != 0 && !getMyAgent().isMatchMakerGotten()) {
      getMyAgent().setMatchmaker(matchers.get(0));
      getMyAgent().setMatchMakerGotten(true);
    }
  }


  @Override
  public boolean done()
  {
    if (getMyAgent().isMatchMakerGotten()) {
      getMyAgent().registerMatcher();
      getMyAgent().addBehaviour(new WaitOpponentBehaviour());
      return true;
    }
    return false;
  }

}
