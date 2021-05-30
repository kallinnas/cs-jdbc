package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import ex.UserAlreadyExistException;
import model.Client;
import model.LoginType;
import model.User;

import java.sql.*;

public class UserDBDao implements UserDao {

    private Connection connection = null;
    private PreparedStatement preStmt = null;
    private CallableStatement callStmt = null;
    private User user = null;

    @Override
    public void createUserCompany(String email, String password) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.INSERT_USER_COMPANY);
            DBUtilSetter.applyUserValuesOnStmt(preStmt, email, password);
            preStmt.execute();
        } catch (SQLException e) {
            throw new SystemMalfunctionException("\"Unable to registrate new user-company!\n" + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }

    @Override
    public void createUserCustomer(String email, String password) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.INSERT_USER_CUSTOMER);
            DBUtilSetter.applyUserValuesOnStmt(preStmt, email, password);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                /* 4-email; 5-password; 2-role; 1-user_id; 3-customer_id */
                user = new User(rs.getString(4), rs.getString(5), rs.getInt(2));
                user.setId(rs.getLong(1));
                user.getClient().setId(rs.getLong(3));
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to registrate new user-customer!\n" + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        connection = ConnectionPool.getInstance().getConnection();
        User user = null;
        ResultSet rs;
        try {
            callStmt = connection.prepareCall("{call user_login(?,?)}");
            DBUtilSetter.applyUserValuesOnStmt(callStmt, email, password);
            callStmt.execute();
            rs = callStmt.getResultSet();
            while (rs.next()) {
                user = DBUtilSetter.resultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to find user with such email *" + email + "*! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return user;
    }

    @Override
    public boolean userEmailIsPresent(String email) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_USER_BY_EMAIL);
            preStmt.setString(1, email);
            /* next() returns false, it means ResultSet is empty */
            return preStmt.executeQuery().next();
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to check if such email *" +
                    email + "* is already exist in DB.\n " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }

}
