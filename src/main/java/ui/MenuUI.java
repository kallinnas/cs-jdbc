package ui;

import java.io.IOException;

public class MenuUI {
    private static final String COMPANY_COUPON_MENU = "1.Create coupon\n2.My coupons\n3.Search coupon\n4.Update coupon\n5.Remove coupon\n6.Go back <--";
    private static final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";
    private static final String COMPANY_MAIN_MENU = "1.Go to coupons\n2.Go to company\n3.Logout\n4.Quit\nUse command numbers to perform: ";
    private static final String COMPANY_UPDATE_MENU = "1.Update company\n2.Search company\n3.Go back <--";
    private static final String COMPANY_SEARCH_COUPON = "FIND BY:\n1.Id\n2.Title\n3.Coupons start from date\n4.Coupons start before date\n5.Price less than\n6.Price more than\n7.Go back <--";


    static void companyMainMenu() {
        System.out.print(COMPANY_MAIN_MENU);
        try {
            switch (RunUI.userCommandNumber()) {
                case 1:
                    MenuUI.companyCouponMenu();
                case 2:
                    MenuUI.companyUpdateMenu();
                case 3:
                    RunUI.logout();
            }
        } catch (NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
            companyMainMenu();
        }
    }

    private static void companyUpdateMenu() {
        System.out.println(COMPANY_UPDATE_MENU);
        switch (RunUI.userCommandNumber()) {
            case 1:
                CompanyUI.updateCompany();
            case 2:
                MenuUI.companyUpdateMenu();
            case 3:
                RunUI.logout();
        }
    }

    static void companyCouponMenu() {
        System.out.println(COMPANY_COUPON_MENU);
        try {
            switch (RunUI.userCommandNumber()) {
                case 1:
                    CompanyUI.createCompanyCoupon();
                    break;
                case 2:
                    CompanyUI.getAllCompanyCoupons();
                    break;
                case 3:
                    searchCouponMenu();
                    break;
                case 4:
                    CompanyUI.updateCoupon();
                    break;
                case 5:
                    CompanyUI.removeCoupon();
                    break;
                case 6:
                    companyMainMenu();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
            companyCouponMenu();
        }
    }

    static void searchCouponMenu() {
        System.out.println(COMPANY_SEARCH_COUPON);
        try {
            switch (RunUI.userCommandNumber()) {
                case 1:
                    CompanyUI.searchCouponById();
                case 2:
                    CompanyUI.searchCouponByTitle();
                case 3:
                    CompanyUI.searchCouponsStartFromDate();
                case 4:
                    CompanyUI.searchCouponsStartBeforeDate();
                case 5:
                    CompanyUI.searchCouponsByPriceLessThan();
                case 6:
                    CompanyUI.searchCouponsByPriceMoreThan();
                case 7:
                    companyCouponMenu();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
            searchCouponMenu();
        }
    }

}
