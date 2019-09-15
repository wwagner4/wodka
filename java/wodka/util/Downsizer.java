package wodka.util;

/**
 * A class that can hold a List always under a certain length.
 */

public class Downsizer {

  private int maxLength;
  private int fixedLength;
  private int compression;
  private int offset;

  public Downsizer(int maxLength, int fixedLength, int compression) {
    this.maxLength = maxLength;
    this.fixedLength = fixedLength;
    this.compression = compression;
    this.offset = fixedLength;
  }
  public void downsize(java.util.List list){
    if (list.size() >= maxLength){
      if ((maxLength - offset - fixedLength) < compression){
        offset = fixedLength;
      }
      int compCount = (maxLength - offset - fixedLength) / compression + 1;
      for (int i=0; i<compCount; i++){
        for (int j=0; j<compression-1; j++){
          int remIndex = maxLength-(offset+2+compression*i+j);
          if (remIndex >= fixedLength){
            list.remove(remIndex);
          }
        }
      }
      offset += compCount + fixedLength;
    }
  }
}