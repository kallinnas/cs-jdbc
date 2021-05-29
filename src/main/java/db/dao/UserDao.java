package db.dao;

import model.LoginType;
import model.User;

public interface UserDao {
    User getUserByEmailAndPassword(String email, String password);

    void createUser(String email, String password, LoginType type);

    boolean userEmailIsPresent(String email);
}
