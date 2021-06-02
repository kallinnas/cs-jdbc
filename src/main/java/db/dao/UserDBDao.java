package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
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
            callStmt = connection.prepareCall("{call insert_user_company(?,?)}");
            DBUtilSetter.applyUserValuesOnStmt(callStmt, email, password);
            ResultSet rs = callStmt.executeQuery();
            rs.next();
            System.out.println("New company with email *" + rs.getString(4) + "* was created under #" + rs.getInt(1) + " successfully!");
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to insert new user-company!\n" + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }

    @Override
    public void createUserCustomer(String email, String password) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            callStmt = connection.prepareCall("{call insert_user_customer(?,?)}");
            DBUtilSetter.applyUserValuesOnStmt(callStmt, email, password);
            ResultSet rs = callStmt.executeQuery();
            rs.next();
            System.out.println("New customer with email *" + rs.getString(4) + "* was created under #" + rs.getInt(1) + " successfully!");
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
