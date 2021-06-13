package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import ex.NoSuchCouponException;
import model.Coupon;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CouponDBDao implements CouponDao {

    private Connection connection = null;
    private PreparedStatement preStmt = null;
    private CallableStatement callStmt = null;
    private Coupon coupon = null;
    private Collection<Coupon> coupons = null;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.CREATE_COUPON);
            DBUtilSetter.applyCouponValuesOnStatement(preStmt, coupon);
            preStmt.executeUpdate();
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            preStmt.setString(1, coupon.getTitle());
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            coupon = DBUtilSetter.resultSetToCoupon(rs);
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
    public Coupon getCouponByTitle(String title) throws NoSuchCouponException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            preStmt.setString(1, title);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            coupon = DBUtilSetter.resultSetToCoupon(rs);
        } catch (SQLException e) {
            throw new NoSuchCouponException();
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupon;
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

    @Override
    public Coupon getCouponById(long id) throws NoSuchCouponException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            coupon = DBUtilSetter.resultSetToCoupon(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupon by id (%d)! (%s) ", id, e.getMessage());
            throw new NoSuchCouponException();
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupon;
    }

    @Override
    public Collection<Coupon> getCouponsStartFromDate(LocalDate startDate) {
        connection = ConnectionPool.getInstance().getConnection();
        coupons = new ArrayList<>();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPONS_START_FROM_DATE);
            preStmt.setDate(1, Date.valueOf(startDate));
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Coupon coupon = DBUtilSetter.resultSetToCoupon(rs);
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons start from (%s)! (%s) ", startDate, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupons;
    }

    @Override
    public Collection<Coupon> getCouponsStartBeforeDate(LocalDate startDate) {
        connection = ConnectionPool.getInstance().getConnection();
        coupons = new ArrayList<>();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPONS_START_BEFORE_DATE);
            preStmt.setDate(1, Date.valueOf(startDate));
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Coupon coupon = DBUtilSetter.resultSetToCoupon(rs);
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons start before (%s)! (%s) ", startDate, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupons;
    }

    @Override
    public Collection<Coupon> getCouponsByPriceLessThan(double price) {
        connection = ConnectionPool.getInstance().getConnection();
        coupons = new ArrayList<>();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPONS_BY_PRICE_LESS_THAN);
            preStmt.setDouble(1, price);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Coupon coupon = DBUtilSetter.resultSetToCoupon(rs);
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons by price less than (%d)! (%s) ", price, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupons;
    }

    @Override
    public Collection<Coupon> getCouponsByPriceMoreThan(double price) {
        connection = ConnectionPool.getInstance().getConnection();
        coupons = new ArrayList<>();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPONS_BY_PRICE_MORE_THAN);
            preStmt.setDouble(1, price);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                Coupon coupon = DBUtilSetter.resultSetToCoupon(rs);
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons by price more than (%d)! (%s) ", price, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupons;
    }

}
