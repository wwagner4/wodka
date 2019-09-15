package wodka;

/**
 * A general runtime exception for wodka.
 */
public class WodkaRuntimeException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public WodkaRuntimeException() {
        super();
    }

    public WodkaRuntimeException(String msg, Throwable exc) {
        super(msg, exc);
    }

    public WodkaRuntimeException(String msg) {
        super(msg);
    }

    public WodkaRuntimeException(Throwable exc) {
        super(exc);
    }


}
