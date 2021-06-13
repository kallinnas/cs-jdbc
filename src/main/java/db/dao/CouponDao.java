package db.dao;

import ex.NoSuchCouponException;
import model.Coupon;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public interface CouponDao {
    Coupon createCoupon(Coupon coupon);

    Coupon getCouponByTitle(String title) throws NoSuchCouponException;

    Collection<Coupon> getCouponsByCompanyId(long id);

    void removeCoupon(long id) throws SQLException;

    Coupon getCouponById(long id) throws NoSuchCouponException;

    Collection<Coupon> getCouponsStartFromDate(LocalDate startDate);

    Collection<Coupon> getCouponsStartBeforeDate(LocalDate startDate);

    Collection<Coupon> getCouponsByPriceLessThan(double price);

    Collection<Coupon> getCouponsByPriceMoreThan(double price);
}
