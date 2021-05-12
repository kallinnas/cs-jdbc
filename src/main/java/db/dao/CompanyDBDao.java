package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.Schema;
import ex.NoSuchCompanyException;
import model.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            stmt = connection.prepareStatement(Schema.REMOVE_COMPANY);
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

    private void applyCompanyValuesOnStatement(PreparedStatement stmt, Company company) throws SQLException {
        stmt.setString(1, company.getName());
        stmt.setString(2, company.getImageURL());
    }
}
