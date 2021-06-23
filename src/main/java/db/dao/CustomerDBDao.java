package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import ex.NoSuchCustomerException;
import facade.CustomerFacade;
import lombok.Setter;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDBDao implements CustomerDao {

    @Setter
    protected Customer customer;

    private Connection connection = null;
    private PreparedStatement preStmt = null;

    @Override
    public Customer getCustomer() {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_ID);
            preStmt.setLong(1, new CustomerFacade().getUser().getClient().getId());
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            customer = DBUtilSetter.resultSetToCustomer(rs);
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get company!");
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) throws NoSuchCustomerException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.UPDATE_CUSTOMER);
            DBUtilSetter.applyCustomerValuesOnStatement(preStmt, customer);
            if (preStmt.executeUpdate() == 0) {
                throw new NoSuchCustomerException("No customer with such id#" + customer.getId());
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to update customer by id#(%d)! (%s) ", customer.getId(), e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customer;
    }

    @Override
    public Customer getCustomerById(long id) throws NoSuchCustomerException {
        connection = ConnectionPool.getInstance().getConnection();
        try{
            preStmt = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            customer = DBUtilSetter.resultSetToCustomer(rs);
        }catch (SQLException e) {
            String msg = String.format("Unable to get customer by id#(%d)! (%s) ", id, e.getMessage());
            throw new NoSuchCustomerException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customer;
    }
}
