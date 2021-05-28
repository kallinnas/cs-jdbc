package facade;

import db.dao.*;
import ex.InvalidLoginException;
import lombok.AllArgsConstructor;
import model.User;

@AllArgsConstructor
public class CustomerFacade extends AbsFacade {
    private CustomerDao customerDao;
    private CouponDao couponDao;

    public static AbsFacade performLogin(String email, String password) throws InvalidLoginException {
        UserDao userDao = new UserDBDao();
        User user = userDao.getUserByEmailAndPassword(email, password);
        if (user != null) return new CustomerFacade(new CustomerDBDao(), new CouponDBDao());
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }
}
