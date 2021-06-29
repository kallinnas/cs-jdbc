package facade.ui;

import db.dao.CompanyDBDao;
import db.dao.CouponDBDao;
import ex.NoSuchCompanyException;
import ex.NoSuchCouponException;
import facade.AbsFacade;
import facade.DisplayDBResult;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collections;

public interface MenuUI {

    String SEARCH_COUPON = "FIND BY:\n1.Id\n2.Title\n3.Coupons start from date\n4.Coupons start before date" +
            "\n5.Price less than\n6.Price more than\n7.Company ID\n8.Go back <--";
    String SEARCH_COMPANY_MENU = "SEARCH BY:\n1.ID\n2.Company name\n3.Go back <--";


    void mainMenu();

    void couponMenu();

    void companyMenu();

    /* METHODS TO READ FROM CONSOLE */
    static int readCommandNumber() throws IOException {
        return Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());
    }

    static String readContext() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    /* DEFAULT METHODS TO SEARCH COMPANY FOR EACH USER ENTITY */
    default void searchCompanyMenu() {
        System.out.println(SEARCH_COMPANY_MENU);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    searchCompanyById();
                    break;
                case 2:
                    searchCompanyByName();
                    break;
                case 3:
                    companyMenu();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        searchCompanyMenu();
    }

    default void searchCompanyByName() {
        while (true) {
            String name = "";
            System.out.print("Enter company name:");
            try {
                name = readContext();
                DisplayDBResult.showCompanyResult(new CompanyDBDao().getCompanyByName(name));
                AbsFacade.closeMenu();
                break;
            } catch (IOException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCompanyException e) {
                System.out.println(String.format(AbsFacade.NO_COMPANY_NAME, name));
            }
        }
        searchCompanyMenu();
    }

    default void searchCompanyById() {
        while (true) {
            long id = 0;
            System.out.print("Enter company id:");
            try {
                id = Long.parseLong(readContext());
                DisplayDBResult.showCompanyResult(Collections
                        .singleton(new CompanyDBDao().getCompanyById(id)));
                AbsFacade.closeMenu();
                break;
            } catch (IOException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCompanyException e) {
                System.out.println(String.format(AbsFacade.NO_COMPANY_ID, id));
            }
        }
        searchCompanyMenu();
    }



    /* DEFAULT METHODS TO SEARCH COUPON FOR EACH USER ENTITY */
    default void searchCouponMenu() {
        System.out.println(SEARCH_COUPON);
        try {
            switch (readCommandNumber()) {
                case 1:
                    searchCouponById();
                case 2:
                    searchCouponByTitle();
                case 3:
                    searchCouponsStartFromDate();
                case 4:
                    searchCouponsStartBeforeDate();
                case 5:
                    searchCouponsByPriceLessThan();
                case 6:
                    searchCouponsByPriceMoreThan();
                case 7:
                    searchCouponsByCompanyId();
                case 8:
                    couponMenu();
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        }
        searchCouponMenu();
    }

    default void searchCouponsByCompanyId() {
        while (true) {
            long id = 0;
            System.out.print("Enter company id:");
            try {
                id = Long.parseLong(readContext());
                if (new CompanyDBDao().getCompanyById(id) != null) {
                    DisplayDBResult.showCouponsResult(new CouponDBDao().getCouponsByCompanyId(id));
                    AbsFacade.closeMenu();
                    break;
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCompanyException e) {
                System.out.println("There is no company with such id #" + id);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsByPriceMoreThan() {
        while (true) {
            System.out.print("Enter price:");
            try {
                val price = Double.parseDouble(readContext());
                val coupons = new CouponDBDao().getCouponsByPriceMoreThan(price);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsByPriceLessThan() {
        while (true) {
            System.out.print("Enter price:");
            try {
                val price = Double.parseDouble(readContext());
                val coupons = new CouponDBDao().getCouponsByPriceLessThan(price);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsStartBeforeDate() {
        while (true) {
            System.out.print("Enter start before date as yyyy-mm-dd:");
            try {
                val startDate = LocalDate.parse(readContext());
                val coupons = new CouponDBDao().getCouponsStartBeforeDate(startDate);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | DateTimeException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsStartFromDate() {
        while (true) {
            System.out.print("Enter start from date as yyyy-mm-dd:");
            try {
                val startDate = LocalDate.parse(readContext());
                val coupons = new CouponDBDao().getCouponsStartFromDate(startDate);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | DateTimeException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponByTitle() {
        System.out.println("Enter coupon title:");
        String title = "";
        try {
            val coupon = new CouponDBDao().getCouponByTitle(title = readContext());
            DisplayDBResult.showCouponsResult(Collections.singletonList(coupon));
            AbsFacade.closeMenu();
        } catch (IOException e) {
            System.out.println(AbsFacade.WRONG_INSERT_MSG);
        } catch (NoSuchCouponException e) {
            System.out.println("No coupon with title as: " + title);
        }
        searchCouponMenu();
    }

    default void searchCouponById() {
        while (true) {
            long id = 0;
            System.out.print("Enter coupon id:");
            try {
                id = Long.parseLong(readContext());
                val coupon = new CouponDBDao().getCouponById(id);
                DisplayDBResult.showCouponsResult(Collections.singleton(coupon));
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCouponException e) {
                System.out.println(String.format(AbsFacade.NO_COUPON, id));
            }
        }
        searchCouponMenu();
    }
}
