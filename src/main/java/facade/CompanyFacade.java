package facade;

import db.dao.*;
import ex.InvalidLoginException;
import lombok.AllArgsConstructor;
import model.User;

@AllArgsConstructor
public class CompanyFacade extends AbsFacade{
    private CompanyDao companyDao;
    private CouponDao couponDao;

    public static AbsFacade performLogin(String email, String password) throws InvalidLoginException {
        UserDao userDao = new UserDBDao();
        User user = userDao.getUserByEmailAndPassword(email, password);
        if (user.getEmail() == email && user.getPassword() == password) return new CompanyFacade(new CompanyDBDao(), new CouponDBDao());
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }
}
