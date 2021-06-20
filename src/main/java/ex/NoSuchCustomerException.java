package ex;

public class NoSuchCustomerException extends Exception {
    public NoSuchCustomerException(String msg) {
        super(msg);
    }
}
