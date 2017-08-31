package negotiator.behaviour;

import jade.core.behaviours.OneShotBehaviour;
import negotiator.Negotiator;


/**
 * @author Cedric Buron
 */
class RejectBehaviour extends OneShotBehaviour
{
  private Negotiator getMyAgent(){ return (Negotiator) myAgent;}

  @Override
  public void action()
  {
    getMyAgent().reject();
    getMyAgent().transmitFinished();
    getMyAgent().end();
  }
}
