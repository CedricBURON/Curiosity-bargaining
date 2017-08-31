package common;

import java.util.*;


/**
 * @author Cedric Buron
 */
public abstract class Global
{
  public static  int PROTOCOL;
  public static int PORT = 42000;
  public static int NB_AGENTS = 100;
  public static boolean lock;

  /**mean value of a list of double
   *
   * @param list list of double
   * @return the mean value of list
   */
  public static double mean(List<Double> list)
  {
    double mean = 0.0;
    for (double aList : list) {
      mean += aList;
    }
    return mean/(double)list.size();
  }


  /**mean value of a list of int
   *
   * @param list list of int
   * @return the mean of list
   */
  public static double meanInt(List<Integer> list)
  {
    double mean = 0.0;
    for (int aList : list) {
      mean += (double) aList;
    }
    return mean/(double)list.size();
  }
}
