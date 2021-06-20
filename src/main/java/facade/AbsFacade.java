package facade;

import common.SystemMalfunctionException;
import db.dao.*;
import ex.InvalidLoginException;
import ex.UserAlreadyExistException;
import model.Coupon;
import model.LoginType;
import facade.ui.DisplayDBResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

public abstract class AbsFacade {

    private CouponDao couponDao = new CouponDBDao();
    private UserDao userDao = new UserDBDao();
    private Collection<Coupon> coupons;

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";

    public static AbsFacade login(String email, String password, LoginType type) throws InvalidLoginException {
        switch (type) {
            case ADMIN:
                AdminFacade adminFacade = new AdminFacade();
                return adminFacade.performLogin(email, password);
            case CUSTOMER:
                CustomerFacade customerFacade = new CustomerFacade();
                return customerFacade.performLogin(email, password);
            case COMPANY:
                CompanyFacade companyFacade = new CompanyFacade();
                return companyFacade.initFacade(email, password);
            default:
                throw new SystemMalfunctionException("Unable to recognize such login type for current credentials!");
        }
    }

    public void registerUser(String email, String password, LoginType type) throws InvalidLoginException, UserAlreadyExistException {
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

    public LoginType userRole(String email) {
        if (email.equals("admin")) return LoginType.ADMIN;
        return userDao.getUserRoleByEmail(email);
    }

    public void getAllCoupons() {
        coupons = couponDao.getAllCoupons();
        DisplayDBResult.showCouponResult(coupons);
        closeMenu();
    }

    /**
     * Method print out message for returning ti the previous menu.
     * Other facades that inheritance from AbsFacade should be able
     * to use this method - must be protected.
     */
    protected void closeMenu() {
        String GO_BACK_MSG = "Return to menu just press Enter";
        System.out.println(GO_BACK_MSG);
        try {
            reader.readLine();
        } catch (IOException e) {
            // ignore
        }
    }

}
