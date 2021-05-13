package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.Schema;
import model.Coupon;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CouponDBDao implements CouponDao {

//    private Connection connection = null;
    private PreparedStatement stmt = null;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            stmt = connection.prepareStatement(Schema.CREATE_COUPON);
            stmt.setLong(1, coupon.getCompanyId());
            stmt.setString(2, coupon.getTitle());
            stmt.setDate(3, Date.valueOf(coupon.getStartDate()));
            stmt.setDouble(4, coupon.getPrice());
            stmt.setString(5, coupon.getDescription());
            stmt.setString(6, coupon.getImageURL());
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = String.format("Unable to create new coupon! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return coupon;
    }

    @Override
    public Coupon getCouponByTitle(String title) {
        Set<Coupon> coupons = new HashSet<>();
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            stmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Coupon coupon = new Coupon();
                coupon.setId(rs.getLong(1));
                coupon.setCompanyId(rs.getLong(2));
                coupon.setTitle(rs.getString(3));
                coupon.setStartDate(rs.getDate(4).toLocalDate());
                coupon.setPrice(rs.getDouble(5));
                coupon.setDescription(rs.getString(6));
                coupon.setImageURL(rs.getString(7));
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to create new coupon! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return coupons.iterator().next();
    }
}
