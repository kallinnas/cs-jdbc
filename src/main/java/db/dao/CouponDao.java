package db.dao;

import model.Coupon;

public interface CouponDao {
    Coupon createCoupon(Coupon coupon);

    Coupon getCouponByTitle(String title);
}
