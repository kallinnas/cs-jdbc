package ex;

public class CompanyAlreadyExistException extends Throwable {
    public CompanyAlreadyExistException(String msg) {
        super(msg);
    }
}
