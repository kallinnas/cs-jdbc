package facade;

import ex.InvalidLoginException;
import model.LoginType;

public class AbsFacade {
    protected AbsFacade login(String email, String password, LoginType type) throws InvalidLoginException {
        switch (type) {
            case ADMIN:
                return AdminFacade.performLogin(email, password);
            case COMPANY:
                return CompanyFacade.performLogin(email, password);
            case CUSTOMER:
                return CustomerFacade.performLogin(email, password);
            default:
                throw new InvalidLoginException("Unable to recognize login type!");
        }
    }
}
