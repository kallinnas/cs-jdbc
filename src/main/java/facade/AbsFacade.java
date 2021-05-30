package facade;

import db.dao.UserDBDao;
import db.dao.UserDao;
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

    public AbsFacade register(String email, String password, LoginType type) {
        UserDao userDao = new UserDBDao();
        if (!userDao.userEmailIsPresent(email)) {
            switch (type) {
                case COMPANY:
                    userDao.createUserCompany(email, password);
                    break;
                case CUSTOMER:
                    userDao.createUserCustomer(email, password);
                    break;
            }
        }
        return new AbsFacade();
    }
}
