package ui;

import ex.NoSuchCompanyException;
import ex.NoSuchCouponException;
import facade.CompanyFacade;
import model.Company;
import model.Coupon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyUI {

    static CompanyFacade companyFacade;
    static Company company;
    private static boolean isNotRequiredType;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static final String GO_BACK_MSG = "Return to menu just press Enter";
    private static final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";


    static void useCompanyFacade() {
        company = companyFacade.getCompanyDao().getCompany();
        if (company.getName() == null || company.getName().equals("")) {
            updateCompany();
        }
        System.out.println("Welcome to " + company.getName() + " DataBase! ");
        MenuUI.companyMainMenu();
    }

    static void getAllCompanyCoupons() {
        List<Coupon> coupons = companyFacade.getCouponDao().getCouponsByCompanyId(loggedInCompanyId()).stream()
                .sorted(Comparator.comparingLong(Coupon::getId))
                .collect(Collectors.toList());
        DisplayDBResult.showCouponResult(coupons);
        closeMenu();
        MenuUI.companyCouponMenu();
    }

    static void removeCoupon() {
    }

    static void updateCoupon() {
    }

    static void createCompanyCoupon() {
        isNotRequiredType = true;
        Coupon coupon = new Coupon();
        coupon.setCompanyId(loggedInCompanyId());
        try {
            System.out.println("Coupon title: ");
            coupon.setTitle(reader.readLine());
            while (isNotRequiredType) {
                System.out.println("Coupon price(double): ");
                try {
                    double price = Double.parseDouble(reader.readLine());
                    coupon.setPrice(price);
                    isNotRequiredType = false;
                } catch (NumberFormatException e) {
                    System.out.println(WRONG_INSERT_MSG);
                }
            }
            System.out.println("Coupon description: ");
            coupon.setDescription(reader.readLine());
            System.out.println("Coupon image: ");
            coupon.setImageURL(reader.readLine());
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        Coupon c = companyFacade.getCouponDao().createCoupon(coupon);
        System.out.println("New coupon #" + c.getId() + " " + c.getTitle() + " was created successfully!");
        MenuUI.companyCouponMenu();
    }


    private static long loggedInCompanyId() {
        return CompanyFacade.getUser().getClient().getId();
    }

    public static void searchCouponByTitle() {
        System.out.println("Enter coupon title:");
        Coupon coupon = null;
        String title = "";
        try {
            coupon = companyFacade.getCouponDao().getCouponByTitle(title = reader.readLine());
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        } catch (NoSuchCouponException e) {
            System.out.println("No coupon with such title: " + title);
            MenuUI.searchCouponMenu();
        }
        System.out.println(coupon);
        MenuUI.searchCouponMenu();
    }


    static void searchCouponById() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            long id = 0;
            System.out.print("Enter coupon id:");
            try {
                id = Long.parseLong(reader.readLine());
                isNotRequiredType = false;
                Coupon coupon = companyFacade.getCouponDao().getCouponById(id);
                System.out.println(coupon);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            } catch (NoSuchCouponException e) {
                System.out.println("There is no coupon with such id: " + id);
            }
        }
        MenuUI.searchCouponMenu();
    }

    public static void searchCouponsStartFromDate() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter start date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = companyFacade.getCouponDao().getCouponsStartFromDate(startDate);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
                MenuUI.searchCouponMenu();
            }
        }
        MenuUI.searchCouponMenu();
    }

    public static void searchCouponsStartBeforeDate() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter start date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = companyFacade.getCouponDao().getCouponsStartBeforeDate(startDate);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
                MenuUI.searchCouponMenu();
            }
        }
        MenuUI.searchCouponMenu();
    }


    public static void searchCouponsByPriceLessThan() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = companyFacade.getCouponDao().getCouponsByPriceLessThan(price);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
                MenuUI.searchCouponMenu();
            }
        }
        MenuUI.searchCouponMenu();
    }

    static void searchCouponsByPriceMoreThan() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = companyFacade.getCouponDao().getCouponsByPriceMoreThan(price);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
                MenuUI.searchCouponMenu();
            }
        }
        MenuUI.searchCouponMenu();
    }

    private static void closeMenu() {
        System.out.println(GO_BACK_MSG);
        try {
            reader.readLine();
        } catch (IOException e) {
            // ignore
        }
    }

    /* LOGIN METHODS */


    static void updateCompany() {
        try {
            System.out.print("Lets set a name for your company: ");
            String line = reader.readLine();
            company.setName(line);
            System.out.print("What about the logo? Add url: ");
            company.setImageURL(reader.readLine());
            try {
                company = companyFacade.getCompanyDao().updateCompany(company);
                System.out.println("Company name " + company.getName() + " was just set successfully!");
            } catch (NoSuchCompanyException e) {
                // ignored
            }
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
    }


}
