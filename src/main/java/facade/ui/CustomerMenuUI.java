package facade.ui;

import facade.AbsFacade;
import facade.CustomerFacade;
import facade.DisplayDBResult;
import lombok.Setter;

import java.io.IOException;

public class CustomerMenuUI implements MenuUI {

    @Setter
    protected CustomerFacade facade;
    private final String MAIN_MENU = "1.Go to coupons\n2.Go to companies\n3.My Account\n4.Logout\n5.Quit\nUse command numbers to perform: ";
    private final String COUPON_MENU = "1.Show all coupons\n2.My coupons\n3.Search coupon\n4.Purchase coupon\n5.Send coupon\n6.Go back <--";
    private final String COMPANY_MENU = "1.Show all companies\n2.Search company\n3.Go back <--";;

    @Override
    public void mainMenu() {
        System.out.print(MAIN_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    couponMenu();
                case 2:
                    companyMenu();
                    break;
                case 3:
                    DisplayDBResult.showMyAccount(facade.getUser());
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
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
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
                    facade.getAllCustomerCoupons();
                    break;
                case 3:
                    searchCouponMenu();
                    break;
                case 4:
                    facade.purchaseCoupon();
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
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
            couponMenu();
        }
        couponMenu();
    }

    @Override
    public void companyMenu() {
        System.out.println(COMPANY_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    AbsFacade.getAllCompanies();
                    break;
                case 2:
                    searchCompanyMenu();
                    break;
                case 3:
                    mainMenu();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        companyMenu();
    }

}
