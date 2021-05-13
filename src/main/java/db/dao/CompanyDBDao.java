package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.Schema;
import ex.NoSuchCompanyException;
import model.Company;
import model.Coupon;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CompanyDBDao implements CompanyDao {

    private Connection connection = ConnectionPool.getInstance().getConnection();
    private PreparedStatement stmt = null;

    @Override
    public Company createCompany(Company company) {
        try {
            stmt = connection.prepareStatement(Schema.INSERT_COMPANY);
            applyCompanyValuesOnStatement(stmt, company);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = String.format("Unable to create new company! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return company;
    }

    @Override
    public void removeCompany(long id) throws NoSuchCompanyException {
        try {
            stmt = connection.prepareStatement(Schema.DELETE_COMPANY);
            stmt.setLong(1, id);
            if (stmt.executeUpdate() == 0) {
                throw new NoSuchCompanyException("No company with such id#" + id);
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to remove company by id#((%d))! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
    }

    @Override
    public Company updateCompany(Company company) throws NoSuchCompanyException {
        try {
            stmt = connection.prepareStatement(Schema.UPDATE_COMPANY);
            applyCompanyValuesOnStatement(stmt, company);
            stmt.setLong(3, company.getId());
            if (stmt.executeUpdate() == 0) {
                throw new NoSuchCompanyException("No company with such id#" + company.getId());
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to update company by id#(%d)! (%s) ", company.getId(), e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return company;
    }

    @Override
    public Collection<Coupon> getCompanyCoupons(long id) throws NoSuchCompanyException {
        Set<Coupon> coupons = new HashSet<>();
        try {
            stmt = connection.prepareStatement(Schema.SELECT_COMPANY_COUPONS);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            coupons = resultSetToCoupon(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to update company by id#(%d)! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return coupons;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        Set<Coupon> coupons = new HashSet<>();
        try {
            stmt = connection.prepareStatement(Schema.CREATE_COUPON);
            applyCouponValuesOnStatement(stmt, coupon);
            stmt.executeUpdate();
            stmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            stmt.setString(1, coupon.getTitle());
            ResultSet rs = stmt.executeQuery();
            coupons = resultSetToCoupon(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to create new coupon! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return coupons.iterator().next();
    }

    private void applyCouponValuesOnStatement(PreparedStatement stmt, Coupon coupon) throws SQLException {
        stmt.setLong(1, coupon.getCompanyId());
        stmt.setString(2, coupon.getTitle());
        stmt.setDate(3, Date.valueOf(coupon.getStartDate()));
        stmt.setDouble(4, coupon.getPrice());
        stmt.setString(5, coupon.getDescription());
        stmt.setString(6, coupon.getImageURL());
    }

    private Set<Coupon> resultSetToCoupon(ResultSet rs) throws SQLException {
        Set<Coupon> coupons = new HashSet<>();
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
        return coupons;
    }

    private void applyCompanyValuesOnStatement(PreparedStatement stmt, Company company) throws SQLException {
        stmt.setString(1, company.getName());
        stmt.setString(2, company.getImageURL());
    }
}
