package common;

/**Class representing the parameters of a unique experiment
 *
 * @author Cedric Buron
 */
public class Parameter
{
  private final int protocol;
  private final double sellerCuriousProportion;
  private final double sellerSecretiveProportion;
  private final double purchaserCuriousProportion;
  private final double purchaserSecretiveProportion;
  private final long seed;


  /** proportion of curious sellers + proportion of secretive sellers + proportion of uncurious sellers = 1
   * proportion of curious purchasers + proportion of secretive purchasers + proportion of uncurious purchasers = 1
   * @param protocol true: our protocol; false: negotiation
   * @param sellerCuriousProportion proportion of curious sellers
   * @param sellerSecretiveProportion proportion of secretive sellers
   * @param purchaserCuriousProportion proportion of curious purchasers
   * @param purchaserSecretiveProportion proportion of secretive purchasers
   * @param seed
   */
  public Parameter(int protocol, double sellerCuriousProportion, double sellerSecretiveProportion,
                   double purchaserCuriousProportion, double purchaserSecretiveProportion, long seed)
  {
    this.protocol = protocol;
    this.sellerCuriousProportion = sellerCuriousProportion;
    this.sellerSecretiveProportion = sellerSecretiveProportion;
    this.purchaserCuriousProportion = purchaserCuriousProportion;
    this.purchaserSecretiveProportion = purchaserSecretiveProportion;
    this.seed=seed;
  }


  public int getProtocol()
  {
    return protocol;
  }


  public double getSellerCuriousProportion()
  {
    return sellerCuriousProportion;
  }


  public double getSellerSecretiveProportion()
  {
    return sellerSecretiveProportion;
  }


  public double getPurchaserCuriousProportion()
  {
    return purchaserCuriousProportion;
  }


  public double getPurchaserSecretiveProportion()
  {
    return purchaserSecretiveProportion;
  }

  public double getPurchaserUncuriousProportion()
  {
    return 1-purchaserCuriousProportion-purchaserSecretiveProportion;
  }

  public double getSellerUncuriousProportion()
  {
    return 1-sellerCuriousProportion-sellerSecretiveProportion;
  }


  public long getSeed()
  {
    return seed;
  }
}
