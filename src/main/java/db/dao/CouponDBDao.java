package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import model.Coupon;

import java.sql.*;
import java.util.Collection;
import java.util.Set;

public class CouponDBDao implements CouponDao {

    private Connection connection = null;
    private PreparedStatement preStmt = null;
    private CallableStatement callStmt = null;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.CREATE_COUPON);
            DBUtilSetter.applyCouponValuesOnStatement(preStmt, coupon);
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
            coupons = DBUtilSetter.resultSetToCouponSet(rs);
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
            coupons = DBUtilSetter.resultSetToCouponSet(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons with companyId (%d)! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }

    @Override
    public void removeCoupon(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            callStmt = connection.prepareCall("{call delete_coupon(?)}");
            callStmt.setLong(1, id);
            callStmt.execute();
        } catch (SQLException e) {
            String msg = String.format("Unable to remove coupon with id (%d)! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }


}
