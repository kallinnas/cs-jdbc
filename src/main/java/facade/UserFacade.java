package facade;

import db.dao.UserDBDao;
import db.dao.UserDao;
import ex.InvalidLoginException;
import model.LoginType;


public class UserFacade {

    public static void registrationNewUser(String email, String password, LoginType type) throws InvalidLoginException {
        UserDao userDao = new UserDBDao();
        if (userDao.userEmailIsPresent(email)) {
            userDao.createUser(email, password, type);
        } else throw new InvalidLoginException("User with such email *" + email + "* already exist!");
    }
}
