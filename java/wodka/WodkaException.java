/*
 * WodkaException.java
 *
 * Created on 26. November 2002, 18:13
 */

package wodka;


/** Indicates the occurrence of a wodka specific exception.
 * @author wwagner4
 */
public class WodkaException extends java.lang.Exception {

    private static final long serialVersionUID = 1L;
    public WodkaException() {
        super();
    }
    public WodkaException(String message) {
        super(message);
    }
    public WodkaException(String message, Throwable cause) {
        super(message, cause);
    }
    public WodkaException(Throwable cause) {
        super(cause);
    }
}
