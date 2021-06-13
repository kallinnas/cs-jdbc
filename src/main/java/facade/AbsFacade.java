package facade;

import common.SystemMalfunctionException;
import db.dao.UserDBDao;
import db.dao.UserDao;
import ex.InvalidLoginException;
import ex.UserAlreadyExistException;
import model.LoginType;

public abstract class AbsFacade {

    public static AbsFacade login(String email, String password, LoginType type) throws InvalidLoginException {
        switch (type) {
            case ADMIN:
                return AdminFacade.performLogin(email, password);
            case CUSTOMER:
                return CustomerFacade.performLogin(email, password);
            case COMPANY:
                return CompanyFacade.performLogin(email, password);
            default:
                throw new SystemMalfunctionException("Unable to recognize such login type for current credentials!");
        }
    }

    public void registerUser(String email, String password, LoginType type) throws InvalidLoginException, UserAlreadyExistException {
        UserDao userDao = new UserDBDao();
        if (!userDao.userEmailIsPresent(email)) {
            switch (type) {
                case COMPANY:
                    userDao.createUserCompany(email, password);
                    login(email, password, type);
                    break;
                case CUSTOMER:
                    userDao.createUserCustomer(email, password);
                    login(email, password, type);
                    break;
            }
        } else throw new UserAlreadyExistException("User with such email *" + email + "* already exist in DB");
    }

    public static LoginType userRole(String email) {
        return new UserDBDao().getUserRoleByEmail(email);
    }
}
