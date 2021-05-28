package ex;

public class NoSuchUserException extends Throwable {
    public NoSuchUserException(String msg) {
        super(msg);
    }
}
