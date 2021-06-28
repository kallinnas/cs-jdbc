package facade.ui;

import db.dao.CompanyDBDao;
import db.dao.CouponDBDao;
import ex.NoSuchCompanyException;
import ex.NoSuchCouponException;
import facade.AbsFacade;
import facade.DisplayDBResult;
import model.Coupon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public interface MenuUI {

    String WRONG_INSERT_MSG = "Wrong command number. Try more. ";
    String SEARCH_COUPON = "FIND BY:\n1.Id\n2.Title\n3.Coupons start from date\n4.Coupons start before date" +
            "\n5.Price less than\n6.Price more than\n7.Company ID\n8.Go back <--";

    void mainMenu();

    void couponMenu();

    void updateMenu();

    static int readCommandNumber() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }

    /* default methods for search coupon for all menus */
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
            System.out.println(WRONG_INSERT_MSG);
        }
        searchCouponMenu();
    }

    default void searchCouponsByCompanyId() {
        while (true) {
            long id = 0;
            System.out.print("Enter company id:");
            try {
                id = Long.parseLong(AbsFacade.reader.readLine());
                if (new CompanyDBDao().getCompanyById(id) == null) {
                    System.out.println("There is no company with such id #" + id);
                    searchCouponMenu();
                }
                DisplayDBResult.showCouponsResult(new CouponDBDao().getCouponsByCompanyId(id));
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException | NoSuchCompanyException e) {
                System.out.println(WRONG_INSERT_MSG);
                searchCouponMenu();
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsByPriceMoreThan() {
        while (true) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(AbsFacade.reader.readLine());
                Collection<Coupon> coupons = new CouponDBDao().getCouponsByPriceMoreThan(price);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsByPriceLessThan() {
        while (true) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(AbsFacade.reader.readLine());
                Collection<Coupon> coupons = new CouponDBDao().getCouponsByPriceLessThan(price);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsStartBeforeDate() {
        while (true) {
            System.out.print("Enter start before date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(AbsFacade.reader.readLine());
                Collection<Coupon> coupons = new CouponDBDao().getCouponsStartBeforeDate(startDate);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponsStartFromDate() {
        while (true) {
            System.out.print("Enter start from date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(AbsFacade.reader.readLine());
                Collection<Coupon> coupons = new CouponDBDao().getCouponsStartFromDate(startDate);
                DisplayDBResult.showCouponsResult(coupons);
                AbsFacade.closeMenu();
                break;
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        searchCouponMenu();
    }

    default void searchCouponByTitle() {
        System.out.println("Enter coupon title:");
        String title = "";
        try {
            Coupon coupon = new CouponDBDao().getCouponByTitle(title = AbsFacade.reader.readLine());
            DisplayDBResult.showCouponsResult(Collections.singletonList(coupon));
            AbsFacade.closeMenu();
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        } catch (NoSuchCouponException e) {
            System.out.println("No coupon with such title: " + title);
            searchCouponMenu();
        }
        searchCouponMenu();
    }

    default void searchCouponById() {
        while (true) {
            long id = 0;
            System.out.print("Enter coupon id:");
            try {
                id = Long.parseLong(AbsFacade.reader.readLine());
                Coupon coupon = new CouponDBDao().getCouponById(id);
                DisplayDBResult.showCouponsResult(Collections.singleton(coupon));
                AbsFacade.closeMenu();
                break;
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            } catch (NoSuchCouponException e) {
                System.out.println("There is no coupon with such id: " + id);
            }
        }
        searchCouponMenu();
    }
}
