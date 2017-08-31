package common;

/**
 * @author Cedric Buron
 */
public enum AgentType
{
  SECRETIVE ("Secretive"){
    public double logarithmDiscount(int step, double param)
  {
    return Math.log(4+step*param)/Math.log(4);
  }
  },

  //With some values of
  CURIOUS ("Curious"){
      public double logarithmDiscount(int step, double param)
      {
        return Math.log(2+step*param)/Math.log(2);
      }
    },
  UNCURIOUS ("Uncurious");

  private String name = "";



  //Constructor

  AgentType(String name){

    this.name = name;

  }


  public static AgentType fromString(String s){
    if(SECRETIVE.name.equals(s)){
      return SECRETIVE;
    }
    else if(CURIOUS.name.equals(s)){
      return CURIOUS;
    }
    else if(UNCURIOUS.name.equals(s)){
      return UNCURIOUS;
    }
    else throw new IllegalArgumentException("error, this string: " + s + " does not match any element of the enum");
  }


  public String toString(){

    return name;
  }


  public double logarithmDiscount(int step, double param){
    return 1;
  }
}
