package facade.ui;

import facade.CompanyFacade;
import lombok.Setter;

import java.io.IOException;

public class CompanyMenuUI implements MenuUI {

    @Setter
    protected CompanyFacade facade;

    private final String MAIN_MENU = "1.Go to coupons\n2.Go to company\n3.Logout\n4.Quit\nUse command numbers to perform: ";
    private final String COUPON_MENU = "1.Create coupon\n2.My coupons\n3.Search coupon\n4.Update coupon\n5.Remove coupon\n6.Go back <--";
    private final String UPDATE_MENU = "1.Update company\n2.Go back <--";

    @Override
    public void mainMenu() {
        System.out.print(MAIN_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    couponMenu();
                case 2:
                    updateMenu();
                case 3:
                    new MenuUIController().logout();
                case 4:
                    MenuUIController.stopApp();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            mainMenu();
        }
    }

    @Override
    public void couponMenu() {
        System.out.println(COUPON_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    facade.createCompanyCoupon();
                    break;
                case 2:
                    facade.getAllCompanyCoupons();
                    break;
                case 3:
                    searchCouponMenu();
                    break;
                case 4:
                    facade.updateCoupon();
                    break;
                case 5:
                    facade.removeCoupon();
                    break;
                case 6:
                    mainMenu();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            couponMenu();
        }
        couponMenu();
    }


    @Override
    public void updateMenu() {
        System.out.println(UPDATE_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    facade.updateCompany();
                case 2:
                    mainMenu();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            updateMenu();
        }
        updateMenu();
    }


}
