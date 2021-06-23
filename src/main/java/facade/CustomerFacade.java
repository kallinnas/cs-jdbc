package facade;

import common.SystemMalfunctionException;
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
        isNotRequiredType = true;
        long newOwnerId = 0;
        try {
            while (isNotRequiredType) {
                System.out.print("Enter coupon id that you want to gift: ");
                long coupon_id = Long.parseLong(reader.readLine());
                coupons = couponDao.getCouponsByCustomerId(customer.getId());
                if (coupons.stream().noneMatch(c -> c.getId() == coupon_id)) {
                    System.out.println("You have no coupon with id " + coupon_id + " in your collection!");
                    ui.couponMenu();
                } else {
                    coupon = coupons.stream().filter(c -> c.getId() == coupon_id).findFirst().get();
                    try {
                        while (isNotRequiredType) {
                            System.out.print("Enter customer id to send a coupon: ");
                            newOwnerId = Long.parseLong(reader.readLine());
                            if (customerDao.getCustomerById(newOwnerId) != null) isNotRequiredType = false;
                        }
                    } catch (IOException | NoSuchCustomerException e) {
                        System.out.println(WRONG_INSERT_MSG + e.getMessage());
                        ui.couponMenu();
                    }
                    if (couponDao.getCouponsByCustomerId(newOwnerId).stream().noneMatch(c -> c.getId() == coupon_id)) {
                        couponDao.purchaseCoupon(newOwnerId, coupon.getId());
                        couponDao.removeCouponFromCustomer(customer.getId(), coupon.getId());
                    } else
                        System.out.println("Unable to send as a gift required coupon. Customer with id #" + newOwnerId +
                                " already has coupon with id #" + coupon_id + ".");
                    ui.couponMenu();
                }
            }
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        System.out.println("Coupon #" + coupon.getId() + " " + coupon.getTitle() +
                " was sent to customer #" + newOwnerId + " successfully!");
        ui.couponMenu();
    }

    public void getAllCustomerCoupons() {
        coupons = couponDao.getCouponsByCustomerId(user.getClient().getId());
        DisplayDBResult.showCouponResult(coupons);
        closeMenu();
        ui.couponMenu();
    }
}
