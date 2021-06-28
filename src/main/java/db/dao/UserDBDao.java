package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import db.Schema;
import model.LoginType;
import model.User;

import java.sql.*;

public class UserDBDao implements UserDao {

    private Connection connection = null;
    private PreparedStatement preStmt = null;
    private CallableStatement callStmt = null;

    @Override
    public void createUserCompany(String email, String password) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            callStmt = connection.prepareCall("{call insert_user_company(?,?)}");
            DBUtilSetter.applyUserValuesOnStmt(callStmt, email, password);
            ResultSet rs = callStmt.executeQuery();
            consoleShowResult(rs);
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
            consoleShowResult(rs);
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to registrate new user-customer!\n" + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
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

    private void consoleShowResult(ResultSet rs) throws SQLException {
        rs.next();
        switch (rs.getInt(2)) {
            case 1:
                System.out.println("New customer with email *" + rs.getString(4) +
                        "* was created under #" + rs.getInt(1) + " successfully!");
                break;
            case 2:
                System.out.println("New user with email *" + rs.getString(4) +
                        "* was created under #" + rs.getInt(1) + " successfully!");
        }
    }

    public LoginType getUserRoleByEmail(String email) {
        connection = ConnectionPool.getInstance().getConnection();
        LoginType type;
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_USER_ROLE_BY_EMAIL);
            preStmt.setString(1, email);
            ResultSet rs = preStmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    type = LoginType.CUSTOMER;
                } else type = LoginType.COMPANY;
            } else type = LoginType.GUEST;
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to check the role. " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
        return type;
    }
}
