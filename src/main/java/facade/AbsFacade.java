package facade;

import common.SystemMalfunctionException;
import db.dao.*;
import ex.InvalidLoginException;
import ex.UserAlreadyExistException;
import facade.ui.MenuUIController;
import model.LoginType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AbsFacade {

    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";

    public static AbsFacade login(String email, String password, LoginType type) throws InvalidLoginException {
        switch (type) {
            case ADMIN:
                AdminFacade adminFacade = new AdminFacade();
                return adminFacade.initFacade(email, password);
            case CUSTOMER:
                CustomerFacade customerFacade = new CustomerFacade();
                return customerFacade.initFacade(email, password);
            case COMPANY:
                CompanyFacade companyFacade = new CompanyFacade();
                return companyFacade.initFacade(email, password);
            default:
                throw new SystemMalfunctionException("Unable to recognize such login type for current credentials!");
        }
    }

    public static AbsFacade registerUser(String email, String password, LoginType type) throws UserAlreadyExistException, InvalidLoginException {
        UserDao dao = new UserDBDao();
        if (!dao.userEmailIsPresent(email)) {
            switch (type) {
                case COMPANY:
                    dao.createUserCompany(email, password);
                    new MenuUIController().initGuestMenuUI(email, password);
                case CUSTOMER:
                    dao.createUserCustomer(email, password);
                    new MenuUIController().initGuestMenuUI(email, password);
                default:
                    throw new NumberFormatException();
            }
        } else throw new UserAlreadyExistException("User with such email *" + email + "* already exist in DB");
    }

    public LoginType userRole(String email) {
        if (email.equals("admin")) return LoginType.ADMIN;
        return new UserDBDao().getUserRoleByEmail(email);
    }

    /**
     * Method displays table with all DB coupons.
     * Method is using by outside package - must be public.
     */
    public void getAllCoupons() {
        DisplayDBResult.showCouponResult(new CouponDBDao().getAllCoupons());
        closeMenu();
    }


    public static void getAllCompanies(){
        DisplayDBResult.showCompanyResult(new CompanyDBDao().getAllCompanies());
        closeMenu();
    }

    /**
     * Method prints out message for returning to the previous menu.
     * Other facades that inheritance from AbsFacade should be able
     * to use this method - must be protected.
     */
    public static void closeMenu() {
        String GO_BACK_MSG = "Return to menu just press Enter";
        System.out.println(GO_BACK_MSG);
        try {
            reader.readLine();
        } catch (IOException e) {
            // ignore
        }
    }

}
