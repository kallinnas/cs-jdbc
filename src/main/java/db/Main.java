package db;

import ex.SystemMalfunctionException;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws SystemMalfunctionException {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        System.out.println(connection);
    }
}
