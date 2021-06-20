package db.dao;

import ex.UserAlreadyExistException;
import model.LoginType;
import model.User;

public interface UserDao {
    User getUserByEmailAndPassword(String email, String password);

    void createUserCompany(String email, String password);

    boolean userEmailIsPresent(String email);

    void createUserCustomer(String email, String password);

    LoginType getUserRoleByEmail(String email);
}
