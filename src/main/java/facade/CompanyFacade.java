package facade;

import db.dao.*;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import ex.NoSuchCouponException;
import lombok.*;
import model.Company;
import model.Coupon;
import model.User;
import facade.ui.CompanyMenuUI;
import facade.ui.DisplayDBResult;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

//@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class CompanyFacade extends AbsFacade {

    private User user;
    private Company company;
    private Coupon coupon;
    @NonNull
    private CompanyMenuUI ui;
    @NonNull
    private CouponDao couponDao = new CouponDBDao();
    private final CompanyDao companyDao = new CompanyDBDao();
    private boolean isNotRequiredType;

    AbsFacade initFacade(String email, String password) throws InvalidLoginException {
        user = new UserDBDao().getUserByEmailAndPassword(email, password);
        company = companyDao.getCompanyById(user.getClient().getId());
        companyDao.setCompany(company);
        // every userInterface needs to have certain facade
        ui = new CompanyMenuUI();
        ui.setFacade(this);
        if (user != null) return this;
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }

    public void runCompanyFacade() {
        company = companyDao.getCompany();
        if (company.getName() == null || company.getName().equals("")) updateCompany();
        System.out.println("Welcome to " + company.getName() + " DataBase! ");
        ui.mainMenu();
    }

    public void updateCompany() {
        try {
            System.out.print("Lets set a name for your company: ");
            company.setName(AbsFacade.reader.readLine());
            System.out.print("What about the logo? Add url: ");
            company.setImageURL(reader.readLine());
            try {
                company = companyDao.updateCompany(company);
                System.out.println("Company name " + company.getName() + " was just set successfully!");
            } catch (NoSuchCompanyException e) {
                // ignored
            }
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
    }

    public void getAllCompanyCoupons() {
        List<Coupon> coupons = couponDao.getCouponsByCompanyId(user.getClient().getId()).stream()
                .sorted(Comparator.comparingLong(Coupon::getId))
                .collect(Collectors.toList());
        DisplayDBResult.showCouponResult(coupons);
        closeMenu();
        ui.couponMenu();
    }

    public void removeCoupon() {


    }

    public void updateCoupon() {


    }

    public void createCompanyCoupon() {
        isNotRequiredType = true;
        coupon = new Coupon();
        coupon.setCompanyId(user.getClient().getId());
        try {
            System.out.println("Coupon title: ");
            coupon.setTitle(reader.readLine());
            while (isNotRequiredType) {
                System.out.println("Coupon price(double): ");
                double price = Double.parseDouble(reader.readLine());
                coupon.setPrice(price);
                isNotRequiredType = false;
            }
            System.out.println("Coupon description: ");
            coupon.setDescription(reader.readLine());
            System.out.println("Coupon image: ");
            coupon.setImageURL(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        Coupon c = couponDao.createCoupon(coupon);
        System.out.println("New coupon #" + c.getId() + " " + c.getTitle() + " was created successfully!");
        ui.couponMenu();
    }

    public void searchCouponByTitle() {
        System.out.println("Enter coupon title:");
        String title = "";
        try {
            coupon = couponDao.getCouponByTitle(title = reader.readLine());
            DisplayDBResult.showCouponResult(Collections.singletonList(coupon));
            closeMenu();
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        } catch (NoSuchCouponException e) {
            System.out.println("No coupon with such title: " + title);
            ui.searchCouponMenu();
        }
        ui.searchCouponMenu();
    }

    public void searchCouponById() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            long id = 0;
            System.out.print("Enter coupon id:");
            try {
                id = Long.parseLong(reader.readLine());
                isNotRequiredType = false;
                coupon = couponDao.getCouponById(id);
                System.out.println(coupon);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            } catch (NoSuchCouponException e) {
                System.out.println("There is no coupon with such id: " + id);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsStartFromDate() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter start from date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsStartFromDate(startDate);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsStartBeforeDate() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter start before date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsStartBeforeDate(startDate);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsByPriceLessThan() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsByPriceLessThan(price);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsByPriceMoreThan() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsByPriceMoreThan(price);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
                ui.searchCouponMenu();
            }
        }
        ui.searchCouponMenu();
    }


}

