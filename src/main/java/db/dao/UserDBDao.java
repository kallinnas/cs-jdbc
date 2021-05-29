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
    public void createUser(String email, String password, LoginType type) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.INSERT_NEW_COMPANY);
            DBUtilSetter.applyUserCompanyValuesOnStmt(preStmt, new User(email, password, type));
            preStmt.execute();
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to create user for new company with such email *" + email + "*! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(callStmt);
        }
    }

    @Override
    public boolean userEmailIsPresent(String email) {
        connection = ConnectionPool.getInstance().getConnection();
        try {
            preStmt = connection.prepareStatement(Schema.SELECT_USER_BY_EMAIL);
            preStmt.setString(1, email);
            return preStmt.execute();
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to create user for new company with such email *" + email + "*! " + e.getMessage());
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
            callStmt.setString(1, email);
            callStmt.setString(2, password);
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

}
