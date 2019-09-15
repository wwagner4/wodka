package wodka.util;

/**
 * A buffer that calculate the mean of a certain amount
 * of values. Uses a fifo inside.
 */

public class MeanBuffer {

  private int[] buf = null;
  private int size;
  private int index = 0;

  public MeanBuffer(int size) {
    this.size = size;
    buf = new int[size];
    for (int i=0; i<size; i++){
      buf[i] = 0;
    }
  }
  public void add(int val){
    buf[index] = val;
    index = (index + 1) % size;
  }
  public int mean(){
    int sum = 0;
    for (int i=0; i<size; i++){
      sum += buf[i];
    }
    return sum / size;
  }
  public void reset(){
    for (int i=0; i<size; i++){
      buf[i] = 0;
    }
  }
}