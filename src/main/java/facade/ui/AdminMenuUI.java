package facade.ui;

import facade.AbsFacade;
import facade.AdminFacade;
import facade.DisplayDBResult;
import lombok.Setter;

import java.io.IOException;

public class AdminMenuUI implements MenuUI{

    @Setter
    protected AdminFacade facade;
    private final String MAIN_MENU = "1.Coupon\n2.Company\n3.Customer\n4.Settings\n5.Logout\n6.Quit\nUse command numbers to perform: ";
    private final String COUPON_MENU = "1.Show all coupons\n2.Search coupon\n3.Update coupon\n4.Remove coupon\n5.Send coupon\n6.Go back <--";

    @Override
    public void mainMenu() {
        System.out.print(MAIN_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    couponMenu();
                case 2:
                    AbsFacade.getAllCompanies();
                    break;
                case 3:
//                    DisplayDBResult.showMyAccount(facade.getUser());
                    AbsFacade.closeMenu();
                    break;
                case 4:
                    new MenuUIController().logout();
                case 5:
                    MenuUIController.stopApp();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        mainMenu();
    }

    @Override
    public void couponMenu() {
        System.out.println(COUPON_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    facade.getAllCoupons();
                    break;
                case 2:
                    searchCouponMenu();
                    break;
                case 3:
                    facade.updateCoupon();
                case 4:
                    facade.removeCoupon();
                    break;
                case 5:
                    facade.sendCoupon();
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

    }

}
