package db;

import ex.SystemMalfunctionException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SystemMalfunctionException, SQLException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        Statement statement = connection.createStatement();

        connection.close();
        statement.close();
//        String q = "SELECT * FROM companies;";
//        ResultSet resultSet = statement.executeQuery(q);
//
//
//        while (resultSet.next()) {
//            Object object = resultSet.getObject(1);
//            System.out.println(object);
//        }
        System.out.println(connection);
    }
}
