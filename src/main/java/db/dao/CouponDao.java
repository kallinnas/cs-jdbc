package db.dao;

import model.Coupon;

import java.sql.SQLException;
import java.util.Collection;

public interface CouponDao {
    Coupon createCoupon(Coupon coupon);

    Coupon getCouponByTitle(String title);

    Collection<Coupon> getCouponsByCompanyId(long id);

    void removeCoupon(long id) throws SQLException;
}
