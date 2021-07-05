package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import ex.NoSuchCustomerException;
import facade.AbsFacade;
import facade.CustomerFacade;
import lombok.Setter;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CustomerDBDao implements CustomerDao {

    @Setter
    protected Customer customer;
    private Collection<Customer> customers;

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
            if (preStmt.executeUpdate() < 1) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new NoSuchCustomerException(AbsFacade.NO_CUSTOMER_ID + customer.getId());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customer;
    }

    @Override
    public Customer getCustomerById(long id) throws NoSuchCustomerException {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            customer = DBUtilSetter.resultSetToCustomer(rs);
        } catch (SQLException e) {
            String msg = String.format("Unable to get customer by id#(%d)! (%s) ", id, e.getMessage());
            throw new NoSuchCustomerException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customer;
    }

    @Override
    public Optional<Customer> getOptCustomerById(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            rs.next();
            customer = DBUtilSetter.resultSetToCustomer(rs);
        } catch (SQLException e) {
            return Optional.empty();
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return Optional.of(customer);
    }

    @Override
    public Collection<Customer> getAllCustomers() {
        customers = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            preStmt = connection.prepareCall(Schema.SELECT_ALL_CUSTOMERS);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                customers.add(DBUtilSetter.resultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all customers! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customers;
    }

    @Override
    public Collection<Customer> getCustomerByFirstName(String name) throws NoSuchCustomerException {
        customers = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            preStmt = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_FIRST_NAME);
            preStmt.setString(1, name);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                customers.add(DBUtilSetter.resultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            throw new NoSuchCustomerException("Unable to get customer by first name " + name + "! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customers;
    }

    @Override
    public Collection<Customer> getCustomerByLastName(String name) {
        customers = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            preStmt = connection.prepareStatement(Schema.SELECT_CUSTOMER_BY_LAST_NAME);
            preStmt.setString(1, name);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                customers.add(DBUtilSetter.resultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get customer by last name " + name + "! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return customers;
    }

    @Override
    public void removeCustomer(long id) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            getCustomerCouponsId(id).forEach(couponId->{
                try {
                    preStmt = connection.prepareStatement(Schema.DELETE_COUPON_FROM_CUSTOMER);
                    preStmt.setLong(1, id);
                    preStmt.setLong(2, couponId);
                    preStmt.execute();
                } catch (SQLException e) {
                    String msg = String.format("Unable to remove company by id#%d! (%s) ", id, e.getMessage());
                    throw new SystemMalfunctionException(msg);
                }
            });

            preStmt = connection.prepareStatement(Schema.DELETE_CUSTOMER);
            preStmt.setLong(1, id);
            preStmt.execute();
            preStmt = connection.prepareStatement(Schema.DELETE_USER_CUSTOMER);
            preStmt.setLong(1, id);
            preStmt.execute();
        } catch (SQLException e) {
            String msg = String.format("Unable to remove customer by id#((%d))! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
    }

    private Collection<Integer> getCustomerCouponsId(long id) {
        Collection<Integer> coupons = new ArrayList<>();
        try {
            connection = ConnectionPool.getInstance().getConnection();
            preStmt = connection.prepareStatement(Schema.SELECT_COUPON_BY_CUSTOMER_ID);
            preStmt.setLong(1, id);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) coupons.add(rs.getInt(2));
        } catch (SQLException e) {
            String msg = String.format("Unable to get all customer coupon by customer_id#(%d)! (%s) ", id, e.getMessage());
            throw new SystemMalfunctionException(msg);
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(preStmt);
        }
        return coupons;
    }
}
