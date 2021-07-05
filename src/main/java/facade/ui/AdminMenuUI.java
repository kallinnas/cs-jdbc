package facade.ui;

import facade.AbsFacade;
import facade.AdminFacade;
import lombok.Setter;

import java.io.IOException;

public class AdminMenuUI implements MenuUI {

    @Setter
    protected AdminFacade facade;
    private final String MAIN_MENU = "1.Coupon\n2.Company\n3.Customer\n4.Settings\n5.Logout\n6.Quit\nUse command numbers to perform: ";
    private final String COUPON_MENU = "1.Show all coupons\n2.Search coupon\n3.Update coupon\n4.Remove coupon\n5.Send coupon\n6.Go back <--";
    private final String COMPANY_MENU = "1.Show all companies\n2.Show all company coupons\n3.Search company\n4.Remove company\n5.Update company\n6.Go back <--";
    private final String CUSTOMER_MENU = "1.Show all customers\n2.Search customer\n3.Update customer\n4.Remove customer\n5.Go back <--";
    private final String SEARCH_CUSTOMER_MENU = "SEARCH BY:\n1.ID\n2.First Name\n3.Last Name\n4.Go back <--";
    private final String SETTINGS_MENU = "1.REMOVE ALL DATA FROM DB\n2.Go back <--";

    @Override
    public void mainMenu() {
        System.out.print(MAIN_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    couponMenu();
                    break;
                case 2:
                    companyMenu();
                    break;
                case 3:
                    customerMenu();
                    break;
                case 4:
                    settingsMenu();
                    break;
                case 5:
                    new MenuUIController().logout();
                case 6:
                    MenuUIController.stopApp();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        mainMenu();
    }

    private void settingsMenu() {
        System.out.print(SETTINGS_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    facade.reloadDB();
                    break;
                case 2:
                    mainMenu();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        mainMenu();
    }

    public void customerMenu() {
        System.out.print(CUSTOMER_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    facade.getAllCustomers();
                    break;
                case 2:
                    searchCustomerMenu();
                    break;
                case 3:
                    facade.updateCustomer();
                    break;
                case 4:
                    facade.removeCustomer();
                    break;
                case 5:
                    mainMenu();
                    break;
                default:
                    throw new IOException();
            }
        } catch (IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        customerMenu();
    }

    public void searchCustomerMenu() {
        System.out.print(SEARCH_CUSTOMER_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    facade.searchCustomerById();
                    break;
                case 2:
                    facade.searchCustomerByFirstName();
                    break;
                case 3:
                    facade.searchCustomerByLastName();
                    break;
                case 4:
                    customerMenu();
                    break;
                default:
                    throw new IOException();
            }
        } catch (IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        searchCompanyMenu();
    }

    @Override
    public void couponMenu() {
        System.out.print(COUPON_MENU);
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
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
            couponMenu();
        }
        couponMenu();
    }

    @Override
    public void companyMenu() {
        System.out.print(COMPANY_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    AbsFacade.getAllCompanies();
                    break;
                case 2:
                    searchCouponsByCompanyId();
                    break;
                case 3:
                    searchCompanyMenu();
                    break;
                case 4:
                    facade.removeCompany();
                    break;
                case 5:
                    facade.updateCompany();
                    break;
                case 6:
                    mainMenu();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        companyMenu();
    }

}
