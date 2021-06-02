package facade;

import db.dao.UserDBDao;
import db.dao.UserDao;
import ex.InvalidLoginException;
import ex.UserAlreadyExistException;
import lombok.Getter;
import model.LoginType;

public class AbsFacade {

    @Getter
    private AbsFacade absFacade;

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

    public void registerUser(String email, String password, LoginType type) throws InvalidLoginException, UserAlreadyExistException {
        UserDao userDao = new UserDBDao();
        if (!userDao.userEmailIsPresent(email)) {
            switch (type) {
                case COMPANY:
                    userDao.createUserCompany(email, password);
                    absFacade = login(email, password, type);
                    break;
                case CUSTOMER:
                    userDao.createUserCustomer(email, password);
                    absFacade = login(email, password, type);
                    break;
            }
        } else throw new UserAlreadyExistException("User with such email *" + email + "* already exist in DB");
    }
}
