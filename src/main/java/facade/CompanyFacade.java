package facade;

import db.dao.*;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import facade.ui.MenuUI;
import lombok.*;
import model.Company;
import model.Coupon;
import model.User;
import facade.ui.CompanyMenuUI;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@RequiredArgsConstructor
public class CompanyFacade extends AbsFacade {

    private User user;
    private Company company;
    private Coupon coupon;

    @NonNull
    private CompanyMenuUI ui;
    @NonNull
    private CouponDao couponDao;
    @NonNull
    private CompanyDao companyDao;
    private boolean isNotRequiredType;

    AbsFacade initFacade(String email, String password) throws InvalidLoginException {
        user = new UserDBDao().getUserByEmailAndPassword(email, password);
        initThis();
        try {
            company = companyDao.getCompanyById(user.getClient().getId());
        } catch (NoSuchCompanyException e) {// ignore
        }
        companyDao.setCompany(company);
        ui.setFacade(this);
        if (user != null) return this;
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }

    private void initThis() {
        this.ui = new CompanyMenuUI();
        this.couponDao = new CouponDBDao();
        this.companyDao = new CompanyDBDao();
    }

    public void runCompanyFacade() {
        if (company.getName() == null || company.getName().equals("")) updateCompany();
        System.out.println("Welcome to " + company.getName() + " DataBase! ");
        ui.mainMenu();
    }

    public void updateCompany() {
        try {
            System.out.print("Lets set a name for your company: ");
            company.setName(MenuUI.readContext());
            System.out.print("What about the logo? Add url: ");
            company.setImageURL(MenuUI.readContext());
            try {
                company = companyDao.updateCompany(company);
                System.out.println("Company name " + company.getName() +
                        " was just set successfully!");
            } catch (NoSuchCompanyException e) {// ignored
            }
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
    }

    public void getAllCompanyCoupons() {
        val coupons = couponDao.getCouponsByCompanyId(user.getClient().getId()).stream()
                .sorted(Comparator.comparingLong(Coupon::getId))
                .collect(Collectors.toList());
        DisplayDBResult.showCouponsResult(coupons);
        closeMenu();
        ui.couponMenu();
    }

    public void createCompanyCoupon() {
        isNotRequiredType = true;
        coupon = new Coupon(user.getClient().getId());
        try {
            System.out.println("Coupon title: ");
            coupon.setTitle(MenuUI.readContext());
            while (isNotRequiredType) {
                System.out.println("Coupon price(double): ");
                coupon.setPrice(Double.parseDouble(MenuUI.readContext()));
                isNotRequiredType = false;
            }
            System.out.println("Coupon description: ");
            coupon.setDescription(MenuUI.readContext());
            System.out.println("Coupon image: ");
            coupon.setImageURL(MenuUI.readContext());
        } catch (IOException | NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        coupon = couponDao.createCoupon(coupon);
        System.out.println("New coupon #" + coupon.getId() + " " +
                coupon.getTitle() + " was created successfully!");
        closeMenu();
        ui.couponMenu();
    }

    public void removeCoupon() {
        isNotRequiredType = true;
        long id = 0;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to delete:");
                getExistCoupon(id = Long.parseLong(MenuUI.readContext()));
            }
            DisplayDBResult.showCouponsResult(Collections.singleton(coupon));
            beforeRemove(id);
        } catch (IOException | SQLException e) {
            System.out.print(String.format(NO_COUPON, id));
            removeCoupon();
        }
        closeMenu();
        ui.couponMenu();
    }

    public void updateCoupon() {
        isNotRequiredType = true;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to update:");
                getExistCoupon(Long.parseLong(MenuUI.readContext()));
            }
            System.out.print("Update coupon title from " + coupon.getTitle() + " to:");
            coupon.setTitle(MenuUI.readContext());
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Update coupon price from " + coupon.getPrice() + " to:");
                coupon.setPrice(Double.parseDouble(MenuUI.readContext()));
                isNotRequiredType = false;
            }
            System.out.print("Update coupon description " + coupon.getDescription() + " to:");
            coupon.setDescription(MenuUI.readContext());
            System.out.print("Update coupon image from " + coupon.getImageURL() + " to:");
            coupon.setImageURL(MenuUI.readContext());
        } catch (IOException e) {
            System.out.print("No coupon with such id or wrong insert.");
            updateCoupon();
        }
        DisplayDBResult.showCouponsResult(Collections
                .singleton(couponDao.updateCoupon(coupon)));
        closeMenu();
        ui.couponMenu();
    }

    // Takes off the load from update and remove Coupon methods
    private void getExistCoupon(long id) {
        couponDao.getCouponsByCompanyId(company.getId()).stream()
                .filter(c -> c.getId() == id).findAny()
                .ifPresentOrElse((value) -> {
                    coupon = value;
                    isNotRequiredType = false;
                }, () -> System.out.println(String.format(NO_COUPON, id)));
    }

    private void beforeRemove(long id) throws IOException, SQLException {
        System.out.print("Are you sure that you want to delete this coupon? Y/N ");
        val userAnswer = MenuUI.readContext();
        switch (userAnswer.toLowerCase()) {
            case "y":
                couponDao.removeCoupon(id);
                System.out.println("Coupon was removed successfully!");
                break;
            case "n":
                System.out.println("Coupon was not removed.");
                break;
            default:
                System.out.println(WRONG_INSERT_MSG);
                beforeRemove(id);
        }
    }


}

