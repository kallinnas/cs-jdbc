package facade;

import db.dao.*;
import ex.InvalidLoginException;
import ex.NoSuchCouponException;
import ex.NoSuchCustomerException;
import lombok.*;
import model.Coupon;
import model.Customer;
import model.User;
import facade.ui.CustomerMenuUI;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class CustomerFacade extends AbsFacade {

    private User user;
    private Customer customer;
    private Coupon coupon;
    private Collection<Coupon> coupons;

    @NonNull
    private CustomerMenuUI ui;
    @NonNull
    private CouponDao couponDao;
    @NonNull
    private CustomerDao customerDao;
    private boolean isNotRequiredType = true;


    AbsFacade initFacade(String email, String password) throws InvalidLoginException {
        user = new UserDBDao().getUserByEmailAndPassword(email, password);
        initThis(new CustomerMenuUI(), new CouponDBDao(), new CustomerDBDao());
        try {
            customer = customerDao.getCustomerById(user.getClient().getId());
        } catch (NoSuchCustomerException e) {
            // ignore
        }
        customerDao.setCustomer(customer);
        ui.setFacade(this);
        if (user != null) return this;
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }

    private void initThis(CustomerMenuUI ui, CouponDBDao couponDBDao, CustomerDBDao customerDBDao) {
        this.ui = ui;
        this.couponDao = couponDBDao;
        this.customerDao = customerDBDao;
    }

    public void runCustomerFacade() {
        if (customer.getFirstName().equals("") || customer.getLastName().equals("")) updateCustomer();
        System.out.println(customer.getFirstName() + " " + customer.getLastName() + " welcome to your Coupon DataBase! ");
        ui.mainMenu();
    }

    private void updateCustomer() {
        try {
            System.out.print("Lets set your first name for your account: ");
            customer.setFirstName(reader.readLine());
            System.out.print("And, of course your last name: ");
            customer.setLastName(reader.readLine());
            try {
                customer = customerDao.updateCustomer(customer);
                System.out.println(customer.getFirstName() + " " + customer.getLastName() + " your name was successfully updated!");
            } catch (NoSuchCustomerException e) {
                // ignored
            }
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
    }

    public void purchaseCoupon() {
        try {
            while (isNotRequiredType) {
                System.out.print("Enter coupon id to purchase it: ");
                coupon = couponDao.getCouponById(Long.parseLong(reader.readLine()));
                isNotRequiredType = false;
            }
        } catch (IOException | NoSuchCouponException e) {
            System.out.println(WRONG_INSERT_MSG);
            purchaseCoupon();
        }
        if (Arrays.stream(couponDao.getCouponsIDByCustomerId(customer.getId())).noneMatch(x -> x == coupon.getId())) {
            coupon = couponDao.purchaseCoupon(customer.getId(), this.coupon.getId());
            System.out.println("Coupon " + coupon.getTitle() + " id " + coupon.getId() + " was purchased successfully!");
            ui.couponMenu();
        } else {
            System.out.println("Customer already has this coupon!");
            isNotRequiredType = true;
            purchaseCoupon();
        }
    }

    public void sendCoupon() {
        AtomicLong ownerId = new AtomicLong(0);
        try {
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Enter coupon id that you want to gift: ");
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
                    () -> {
                        couponDao.purchaseCoupon(ownerId.get(), coupon.getId());
                        couponDao.removeCouponFromCustomer(customer.getId(), coupon.getId());
                    });
        } catch (IOException | NoSuchCustomerException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        System.out.println(String.format(SUCCESS_SENT, coupon.getId(), coupon.getTitle(), ownerId.get()));
        ui.couponMenu();
    }

    private void getExistCoupon(long id) {
        couponDao.getCouponsByCustomerId(customer.getId())
                .stream().filter(c -> c.getId() == id)
                .findAny().ifPresentOrElse((value) -> {
                    coupon = value;
                    isNotRequiredType = false;
                },
                () -> System.out.println(String.format(NO_COUPON, id)));
    }

    public void getAllCustomerCoupons() {
        coupons = couponDao.getCouponsByCustomerId(user.getClient().getId());
        DisplayDBResult.showCouponsResult(coupons);
        closeMenu();
        ui.couponMenu();
    }
}
