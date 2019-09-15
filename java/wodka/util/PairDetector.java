package wodka.util;

/**
 * Detects pairs of integers where the order does not matter.
 */

public class PairDetector {

  private java.util.Set set = new java.util.HashSet();

  public PairDetector() {
      super();
  }
  public void addPair(int a, int b){
    set.add(new Pair(a, b));
  }
  public boolean includesPair(int a, int b){
    return set.contains(new Pair(a, b));
  }
  public String toString() {
    StringBuffer b = new StringBuffer();
    b.append("<");
    java.util.Iterator i = set.iterator();
    while (i.hasNext()){
      b.append(i.next());
    }
    b.append(">");
    return b.toString();
  }
  private class Pair {
    private int a;
    private int b;

    Pair(int a, int b){
      this.a = a;
      this.b = b;
    }
    public boolean equals(Object o){
      Pair p = (Pair)o;
      return ((a == p.a) && (b == p.b)) ||
        ((a == p.b) && (b == p.a));
    }
    public int hashCode(){
      return a+b;
    }
    public String toString(){
      return "["+a+","+b+"]";
    }
  }
}
