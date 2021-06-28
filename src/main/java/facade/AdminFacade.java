package facade;

import db.dao.*;
import ex.CompanyAlreadyExistException;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import ex.NoSuchCustomerException;
import facade.ui.AdminMenuUI;
import lombok.*;
import model.Company;
import model.Coupon;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class AdminFacade extends AbsFacade {

    private final static String LOGIN = "a";
    private final static String PASSWORD = "a";


    @NonNull
    private AdminMenuUI ui;
    @NonNull
    private CouponDao couponDao;
    @NonNull
    private CompanyDao companyDao;
    @NonNull
    private CustomerDao customerDao;
    @NonNull
    private UserDao userDao;

    private boolean isNotRequiredType;

    private Company company;
    private Coupon coupon;
    private Collection<Coupon> coupons;


    AbsFacade initFacade(String email, String password) throws InvalidLoginException {
        if (email.equals(LOGIN) && password.equals(PASSWORD)) {
            initThis();
            ui.setFacade(this);
            return this;
        } else
            throw new InvalidLoginException(String.format("Unable to login with email: %s and password %s", email, password));
    }

    private void initThis() {
        this.ui = new AdminMenuUI();
        this.couponDao = new CouponDBDao();
        this.companyDao = new CompanyDBDao();
        this.customerDao = new CustomerDBDao();
        this.userDao = new UserDBDao();
    }

    public void runAdminFacade() {
        System.out.println("Welcome to Admin control panel!");
        ui.mainMenu();
    }

    /* COMPANY */
    public void createCompany(User user) throws CompanyAlreadyExistException {
        if (!userDao.userEmailIsPresent(user.getEmail())) {
            userDao.createUserCompany(user.getEmail(), user.getPassword());

        }
    }

    public void removeCompany(long id) throws NoSuchCompanyException, SQLException {
        val coupons = couponDao.getCouponsByCompanyId(id);
        for (Coupon coupon : coupons) {
            couponDao.removeCoupon(coupon.getId());
        }
        companyDao.removeCompany(id);
    }

    public void updateCompany(Company company) throws NoSuchCompanyException {
        val result = companyDao.getAllCompanies().stream()
                .filter(c -> c.getId() == company.getId())
                .findAny();
        if (result.isPresent()) companyDao.updateCompany(company);
    }


    public Company getCompanyById(long id) throws NoSuchCompanyException {
        val company = companyDao.getAllCompaniesAndCoupons().stream()
                .filter(c -> c.getId() == id)
                .findAny();
        if (company.isPresent()) return company.get();
        else throw new NoSuchCompanyException("Company with id " + id + " is not exist!");
    }


    /* COUPON */
    public void updateCoupon() {
        isNotRequiredType = true;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to update:");
                val id = Long.parseLong(reader.readLine());
                getExistCoupon(id);
            }
            System.out.print("Update coupon title from " + coupon.getTitle() + " to:");
            coupon.setTitle(reader.readLine());
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Update coupon price from " + coupon.getPrice() + " to:");
                double price = Double.parseDouble(reader.readLine());
                coupon.setPrice(price);
                isNotRequiredType = false;
            }
            System.out.print("Update coupon description " + coupon.getDescription() + " to:");
            coupon.setDescription(AbsFacade.reader.readLine());
            System.out.print("Update coupon image from " + coupon.getImageURL() + " to:");
            coupon.setImageURL(AbsFacade.reader.readLine());
        } catch (IOException e) {
            System.out.print(WRONG_INSERT_MSG);
            updateCoupon();
        }
        DisplayDBResult.showCouponsResult(Collections
                .singleton(couponDao.updateCoupon(coupon)));
        AbsFacade.closeMenu();
        ui.couponMenu();
    }

    public void removeCoupon() {
        isNotRequiredType = true;
        long id = 0L;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to delete:");
                id = Long.parseLong(reader.readLine());
                getExistCoupon(id);
            }
            DisplayDBResult.showCouponsResult(Collections.singleton(coupon));
            beforeRemove(id);
        } catch (IOException | SQLException e) {
            System.out.print(WRONG_INSERT_MSG);
            removeCoupon();
        }
        closeMenu();
        ui.couponMenu();
    }

    // Takes off the load from update and remove Coupon methods
    private void getExistCoupon(long id) {
        couponDao.getOptCouponById(id)
                .ifPresentOrElse((value) -> {
                            coupon = value;
                            isNotRequiredType = false;
                        },
                        () -> System.out.println(String.format(NO_COUPON, id)));
    }

    private void beforeRemove(long id) throws IOException, SQLException {
        System.out.print("Are you sure that you want to delete this coupon? Y/N ");
        String userAnswer = reader.readLine();
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

    public void sendCoupon() {
        AtomicLong ownerId = new AtomicLong(0L);
        try {
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Enter coupon id that you want to send: ");
                getExistCoupon(Long.parseLong(reader.readLine()));
            }
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Enter customer id to send a coupon: ");
                ownerId.set(Long.parseLong(reader.readLine()));
                if (customerDao.getCustomerById(ownerId.get()) != null) isNotRequiredType = false;
            }
            couponDao.getCouponsByCustomerId(ownerId.get()).stream()
                    .filter(c -> c.getId() == coupon.getId())
                    .findAny().ifPresentOrElse((value) -> {
                        System.out.println(String.format(CUSTOMER_HAS_COUPON, ownerId.get(), coupon.getId()));
                        ui.couponMenu();
                    },
                    () -> couponDao.purchaseCoupon(ownerId.get(), coupon.getId()));
        } catch (NoSuchCustomerException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        System.out.println(String.format(SUCCESS_SENT, coupon.getId(), coupon.getTitle(), ownerId.get()));
        ui.couponMenu();
    }
}
