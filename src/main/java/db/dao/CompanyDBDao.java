package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import ex.NoSuchCompanyException;
import model.Company;
import model.Coupon;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CompanyDBDao implements CompanyDao {

    private Connection connection = null;
    private PreparedStatement stmt = null;

    @Override
    public Company createCompany(Company company) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmt = connection.prepareStatement(Schema.INSERT_COMPANY);
            DBUtilSetter.applyCompanyValuesOnStatement(stmt, company);
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
            connection = ConnectionPool.getInstance().getConnection();
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
            connection = ConnectionPool.getInstance().getConnection();
            stmt = connection.prepareStatement(Schema.UPDATE_COMPANY);
            DBUtilSetter.applyCompanyValuesOnStatement(stmt, company);
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
        Set<Coupon> coupons;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmt = connection.prepareStatement(Schema.SELECT_COMPANY_COUPONS);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            coupons = DBUtilSetter.resultSetToCouponSet(rs);
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
    public Collection<Company> getAllCompanies() {
        Set<Company> companies = new HashSet<>();
        CallableStatement cstmt;
        try {
            connection = ConnectionPool.getInstance().getConnection();
//            Statement stmt = connection.createStatement();
//            stmt.execute(Schema.CREATE_PROC_GET_ALL_COMPANIES);
            cstmt = connection.prepareCall("{call get_companies()}");
            boolean hasResult = cstmt.execute();
            if (hasResult) {
                ResultSet rs = cstmt.getResultSet();
                while (rs.next()) {
                    companies.add(DBUtilSetter.resultSetToCompany(rs, 1));
                }
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all companies! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return companies;
    }

    @Override
    public Collection<Company> getAllCompaniesAndCoupons() {
        Set<Company> companies = new HashSet<>();
        CallableStatement cstmt;
        try {
            connection = ConnectionPool.getInstance().getConnection();
//            Statement stmt = connection.createStatement();
//            stmt.execute(Schema.CREATE_PROC_GET_ALL_COMPANIES_AND_COUPONS);
            cstmt = connection.prepareCall("{call get_companies_and_coupons()}");
            boolean hasResult = cstmt.execute();
            if (hasResult) {
                ResultSet resultRow = cstmt.getResultSet();
                Company company = new Company();
                while (resultRow.next()) {
                    if (company.getId() != resultRow.getLong(8)) {
                        Coupon coupon = DBUtilSetter.resultSetToCoupon(resultRow);
                        /* Scroll in current table will be set on column number startOrderNum */
                        company = DBUtilSetter.resultSetToCompany(resultRow, 8);
                        company.getCoupons().add(coupon);
                        companies.add(company);
                    } else {
                        company.getCoupons().add(DBUtilSetter.resultSetToCoupon(resultRow));
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all companies! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
        return companies;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        Set<Coupon> coupons;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            stmt = connection.prepareStatement(Schema.CREATE_COUPON);
            DBUtilSetter.applyCouponValuesOnStatement(stmt, coupon);
            stmt.executeUpdate();
            stmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            stmt.setString(1, coupon.getTitle());
            ResultSet rs = stmt.executeQuery();
            coupons = DBUtilSetter.resultSetToCouponSet(rs);
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
