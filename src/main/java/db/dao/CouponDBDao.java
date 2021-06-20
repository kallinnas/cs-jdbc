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
            coupon = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery()).iterator().next();
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
            coupon = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery()).iterator().next();
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
            coupon = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery()).iterator().next();
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupon by id (%d)! (%s) ", id, e.getMessage());
            throw new NoSuchCouponException();
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
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
            coupons = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery());
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons start from (%s)! (%s) ", startDate, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
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
            coupons = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery());
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons start before (%s)! (%s) ", startDate, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
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
            coupons = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery());
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons by price less than (%d)! (%s) ", price, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
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
            coupons = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery());
        } catch (SQLException e) {
            String msg = String.format("Unable to get coupons by price more than (%d)! (%s) ", price, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }

    @Override
    public Collection<Coupon> getAllCoupons() {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPONS);
            coupons = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery());
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all coupons. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }

    @Override
    public Collection<Coupon> getCouponsByCustomerId(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_CUSTOMER_ID);
            coupons = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery());
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all coupons. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            callStmt = connection.prepareCall("{call update_coupon(?,?,?,?,?)}");
            DBUtilSetter.applyUpdatedCouponValuesOnStatement(callStmt, coupon);
            coupon = DBUtilSetter.resultSetToCouponSet(preStmt.executeQuery()).iterator().next();
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to update coupon. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupon;
    }

    @Override
    public Coupon purchaseCoupon(long customer_id, long coupon_id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            callStmt = connection.prepareCall("{call update_coupon_owner(?,?)}");
            callStmt.setLong(1, customer_id);
            callStmt.setLong(2, coupon_id);
            ResultSet rs = callStmt.executeQuery();
            coupon = DBUtilSetter.resultSetToCouponSet(rs).iterator().next();
        } catch (SQLException e) {
            String msg = String.format("Unable to sell coupon with id (%d)! (%s) ", coupon_id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return coupon;
    }

    @Override
    public void removeCouponFromCustomer(long customer_id, long coupon_id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.DELETE_COUPON_FROM_CUSTOMER);
            preStmt.setLong(1, customer_id);
            preStmt.setLong(2, coupon_id);
            preStmt.execute();
        } catch (SQLException e) {
            String msg = String.format("Unable to delete coupon with id (%d)! (%s) ", coupon_id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
    }

}
