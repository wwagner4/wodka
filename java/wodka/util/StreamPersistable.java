package wodka.util;

import java.io.*;

public interface StreamPersistable extends Serializable {

  public void toStream (DataOutputStream s) throws IOException;
  public void fromStream (DataInputStream s, int version) throws IOException;
  public int getVersion();
}

