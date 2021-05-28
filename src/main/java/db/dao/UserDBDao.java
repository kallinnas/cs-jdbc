package db.dao;

import common.StatementUtils;
import common.SystemMalfunctionException;
import db.ConnectionPool;
import db.DBUtilSetter;
import model.User;

import java.sql.*;

public class UserDBDao implements UserDao {

    private Connection connection = null;
    private PreparedStatement preStmt = null;
    private CallableStatement callStmt = null;

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        connection = ConnectionPool.getInstance().getConnection();
        User user = null;
        ResultSet rs = null;
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
