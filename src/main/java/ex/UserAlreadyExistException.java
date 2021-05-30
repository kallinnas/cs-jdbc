package ex;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
