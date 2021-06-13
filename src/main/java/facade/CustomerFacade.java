package facade;

import db.dao.*;
import ex.InvalidLoginException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.User;

@AllArgsConstructor
public class CustomerFacade extends AbsFacade {
    @Getter
    private static User user;
    private CustomerDao customerDao;
    private CouponDao couponDao;

    public static AbsFacade performLogin(String email, String password) throws InvalidLoginException {
        UserDao userDao = new UserDBDao();
        user = userDao.getUserByEmailAndPassword(email, password);
        if (user != null) return new CustomerFacade(new CustomerDBDao(), new CouponDBDao());
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }
}
