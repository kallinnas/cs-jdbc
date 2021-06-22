package facade.ui;

import facade.CustomerFacade;
import lombok.Setter;

import java.io.IOException;

public class CustomerMenuUI implements MenuUI {

    @Setter
    protected CustomerFacade facade;
    private final String MAIN_MENU = "1.Go to coupons\n2.Go to companies\n3.My Account\n4.Logout\n5.Quit\nUse command numbers to perform: ";
    private final String COUPON_MENU = "1.Show all coupons\n2.My coupons\n3.Search coupon\n4.Purchase coupon\n5.Send coupon\n6.Go back <--";

    @Override
    public void mainMenu() {
        System.out.print(MAIN_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    couponMenu();
                case 2:
                    companyMenu();
                case 3:
                    accountMenu();
                case 4:
                    new GuestMenuUI().logout();
                case 5:
                    GuestMenuUI.stopApp();
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
            System.out.println(WRONG_INSERT_MSG);
            couponMenu();
        }
        couponMenu();
    }

    @Override
    public void companyMenu() {

    }

    @Override
    public void searchCouponMenu() {

    }

    @Override
    public void accountMenu() {

    }

    @Override
    public void updateMenu() {
//        System.out.println(COMPANY_UPDATE_MENU);
//        try {
//            switch (readCommandNumber()) {
//                case 1:
//                    CompanyUI.updateCompany();
//                case 2:
//                    MenuUI.companyUpdateMenu();
//                case 3:
//                    RunUI.logout();
//            }
//        }catch (NumberFormatException | IOException e) {
//            System.out.println(WRONG_INSERT_MSG);
//            mainMenu();
//        }
    }

}
