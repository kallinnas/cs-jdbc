package facade;

import db.dao.*;
import ex.InvalidLoginException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.User;

@AllArgsConstructor
public class CompanyFacade extends AbsFacade{
    @Getter
    private CompanyDao companyDao;
    @Getter
    private CouponDao couponDao;
    @Getter
    private static User user;



    public static AbsFacade performLogin(String email, String password) throws InvalidLoginException {
        UserDao userDao = new UserDBDao();
        user = userDao.getUserByEmailAndPassword(email, password);
        if (user != null) return new CompanyFacade(new CompanyDBDao(), new CouponDBDao());
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }


}
