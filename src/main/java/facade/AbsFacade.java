package facade;

import ex.InvalidLoginException;
import model.LoginType;

public class AbsFacade {
    public AbsFacade login(String email, String password, LoginType type) throws InvalidLoginException {
        switch (type) {
            case ADMIN:
                return AdminFacade.performLogin(email, password);
            case CUSTOMER:
                return CustomerFacade.performLogin(email, password);
            case COMPANY:
                return CompanyFacade.performLogin(email, password);
            default:
                throw new InvalidLoginException("Unable to recognize login type!");
        }
    }

    public AbsFacade register(String email, String password, LoginType type) throws InvalidLoginException {
        UserFacade.registrationNewUser(email, password, type);
        return new AbsFacade();
    }
}
