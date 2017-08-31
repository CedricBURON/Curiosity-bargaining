package goods;

import jade.core.AID;


/**
 * @author Cedric Buron
 */
public interface Good
{
  String getId();
  AID getOwnerAgent();
  void setOwnerAgent(AID ownerAgent);
  String getOwner();

}
