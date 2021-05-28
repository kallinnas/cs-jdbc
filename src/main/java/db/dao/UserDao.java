package db.dao;

import model.User;

public interface UserDao {
    User getUserByEmailAndPassword(String email, String password);
}
