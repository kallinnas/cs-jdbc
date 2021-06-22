package db.dao;

import ex.NoSuchCouponException;
import model.Coupon;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public interface CouponDao {

    Collection<Coupon> getCouponsByCompanyId(long id);

    Collection<Coupon> getCouponsStartFromDate(LocalDate startDate);

    Collection<Coupon> getCouponsStartBeforeDate(LocalDate startDate);

    Collection<Coupon> getCouponsByPriceLessThan(double price);

    Collection<Coupon> getCouponsByPriceMoreThan(double price);

    Collection<Coupon> getAllCoupons();

    long[] getCouponsIDByCustomerId(long id);

    Coupon getCouponById(long id) throws NoSuchCouponException;

    Coupon createCoupon(Coupon coupon);

    Coupon getCouponByTitle(String title) throws NoSuchCouponException;

    Coupon updateCoupon(Coupon coupon);

    Coupon purchaseCoupon(long customer_id, long coupon_id);

    void removeCouponFromCustomer(long customer_id, long coupon_id);

    void removeCoupon(long id) throws SQLException;

    Collection<Coupon> getCouponsByCustomerId(long id);
}
