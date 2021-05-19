package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.Schema;
import model.Coupon;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CouponDBDao implements CouponDao {

    private Connection connection = null;
    private PreparedStatement preStmt = null;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.CREATE_COUPON);
            preStmt.setLong(1, coupon.getCompanyId());
            preStmt.setString(2, coupon.getTitle());
            preStmt.setDate(3, Date.valueOf(coupon.getStartDate()));
            preStmt.setDouble(4, coupon.getPrice());
            preStmt.setString(5, coupon.getDescription());
            preStmt.setString(6, coupon.getImageURL());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            String msg = String.format("Unable to create new coupon! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupon;
    }

    @Override
    public Coupon getCouponByTitle(String title) {
        connection = ConnectionPool.getInstance().getConnection();
        Set<Coupon> coupons;
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            preStmt.setString(1, title);
            ResultSet rs = preStmt.executeQuery();
            coupons = resultSetToCouponSet(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to create new coupon! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons.iterator().next();
    }

    @Override
    public Collection<Coupon> getCouponsByCompanyId(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        Set<Coupon> coupons;
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COMPANY_COUPONS);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            coupons = resultSetToCouponSet(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons with companyId (%d)! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }

    static Set<Coupon> resultSetToCouponSet(ResultSet resultRow) throws SQLException {
        Set<Coupon> coupons = new HashSet<>();
        while (resultRow.next()) {
            coupons.add(resultSetToCoupon(resultRow));
        }
        return coupons;
    }

    static Coupon resultSetToCoupon(ResultSet resultRow) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(resultRow.getLong(1));
        coupon.setCompanyId(resultRow.getLong(2));
        coupon.setTitle(resultRow.getString(3));
        coupon.setStartDate(resultRow.getDate(4).toLocalDate());
        coupon.setPrice(resultRow.getDouble(5));
        coupon.setDescription(resultRow.getString(6));
        coupon.setImageURL(resultRow.getString(7));
        return coupon;
    }
}
