package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import ex.NoSuchCompanyException;
import lombok.Setter;
import model.Company;
import model.Coupon;

import java.sql.*;
import java.util.*;

public class CompanyDBDao implements CompanyDao {

    @Setter
    private Company company;

    private Connection connection = null;
    private PreparedStatement preStmt = null;
    private CallableStatement callStmt = null;

    /**
     * @param company initialized with id, name, imageURL to update company
     *                with same company_id.
     * @return Exist company after update its name and imageURL for logo.
     */
    @Override
    public Company updateCompany(Company company) throws NoSuchCompanyException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.UPDATE_COMPANY);
            DBUtilSetter.applyCompanyValuesOnStatement(preStmt, company);
            if (preStmt.executeUpdate() == 0) {
                throw new NoSuchCompanyException("No company with such id#" + company.getId());
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to update company by id#(%d)! (%s) ", company.getId(), e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return company;
    }

    @Override
    public Company getCompany() {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COMPANY_BY_ID);
            preStmt.setLong(1, company.getId());
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            company = DBUtilSetter.resultSetToCompany(rs, 1);
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get company!");
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return company;
    }

    @Override
    public void removeCompany(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            getCompanyCoupons(id).forEach(c -> {
                try {
                    preStmt = connection.prepareStatement(Schema.DELETE_COMPANY_COUPONS_FROM_CUSTOMER);
                    preStmt.setLong(1, c.getId());
                    preStmt.execute();
                } catch (SQLException e) {
                    String msg = String.format("Unable to remove company by id#%d! (%s) ", id, e.getMessage());
                    throw new SystemMalfunctionException(msg);
                }
            });

            preStmt = connection.prepareStatement(Schema.DELETE_COMPANY_COUPONS);
            preStmt.setLong(1, id);
            preStmt.execute();
            preStmt = connection.prepareStatement(Schema.DELETE_COMPANY);
            preStmt.setLong(1, id);
            preStmt.execute();
            preStmt = connection.prepareStatement(Schema.DELETE_USER_COMPANY);
            preStmt.setLong(1, id);
            preStmt.execute();
        } catch (SQLException e) {
            String msg = String.format("Unable to remove company by id#((%d))! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
    }

    @Override
    public Collection<Coupon> getCompanyCoupons(long id) {
        Collection<Coupon> coupons;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            preStmt = connection.prepareStatement(Schema.SELECT_COMPANY_COUPONS);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            coupons = DBUtilSetter.resultSetToCouponSet(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to update company by id#(%d)! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }

    @Override
    public Collection<Company> getAllCompanies() {
        Collection<Company> companies = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            callStmt = connection.prepareCall("{call get_companies()}");
            boolean hasResult = callStmt.execute();
            if (hasResult) {
                ResultSet rs = callStmt.getResultSet();
                while (rs.next()) {
                    companies.add(DBUtilSetter.resultSetToCompany(rs, 1));
                }
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all companies! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return companies;
    }

    @Override
    public Collection<Company> getAllCompaniesAndCoupons() {
        Set<Company> companies = new HashSet<>();
        CallableStatement cstmt;
        try {
            connection = ConnectionPool.getInstance().getConnection();
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
            StatementUtils.closeAll(preStmt);
        }
        return companies;
    }

    @Override
    public Company getCompanyById(long id) throws NoSuchCompanyException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COMPANY_BY_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            company = DBUtilSetter.resultSetToCompany(rs, 1);
        } catch (SQLException e) {
            throw new NoSuchCompanyException("Unable to get company!");
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return company;
    }

    @Override
    public Collection<Company> getCompanyByName(String name) throws NoSuchCompanyException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COMPANY_BY_NAME);
            preStmt.setString(1, name);
            ResultSet rs = preStmt.executeQuery();
            return DBUtilSetter.resultSetToCompanySet(rs);
        } catch (SQLException e) {
            throw new NoSuchCompanyException("Unable to get company!");
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
    }

    @Override
    public Optional<Company> getOptCompanyById(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_COMPANY_BY_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            company = DBUtilSetter.resultSetToCompany(rs, 1);
        } catch (SQLException e) {
            return Optional.empty();
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return Optional.of(company);
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            preStmt = connection.prepareStatement(Schema.CREATE_COUPON);
            DBUtilSetter.applyCouponValuesOnStatement(preStmt, coupon);
            preStmt.executeUpdate();
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_TITLE);
            preStmt.setString(1, coupon.getTitle());
            ResultSet rs = preStmt.executeQuery();
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


}
