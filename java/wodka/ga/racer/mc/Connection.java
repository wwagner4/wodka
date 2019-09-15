/*
 * Connection.java
 *
 * Created on March 9, 2003, 6:10 PM
 */

package wodka.ga.racer.mc;

/**
 *
 * @author  wolfi
 */
public class Connection {
  
  private int from;
  private int to;
  
  public Connection(int from, int to) {
    this.from = from;
    this.to = to;
  }
  public boolean equals(Object o){
    Connection c = (Connection)o;
    return (c.from == from && c.to == to) || (c.from == to && c.to == from);
  }
  public int hashCode(){
    return from + to;
  }
  public String toString(){
    return "<"+from+"|"+to+">";
  }
}
