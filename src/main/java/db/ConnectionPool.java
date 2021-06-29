package db;

import common.SystemMalfunctionException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static ConnectionPool instance;
    private BlockingQueue<Connection> connections;
    private static final int MAX_CONNECTIONS = 3;
    private static final String CONFIG_DB_PROPERTIES = "src/main/resources/config.properties";
    private static final String CREATE_TABLES = "src/main/resources/createTables";
    private static final String DROP_TABLES = "src/main/resources/dropTables";

    private ConnectionPool() {
        this.connections = new LinkedBlockingQueue<>(MAX_CONNECTIONS);
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            connections.offer(createConnection());
        }
    }

    public Connection getConnection() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            String msg = String.format("Unable to get Connection! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
    }

    public synchronized void putConnection(Connection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            String msg = String.format("Unable to get Connection! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
    }

    synchronized void closeAllConnections() {
        Connection connection;
        try {
            while ((connection = connections.poll()) != null) {
                connection.close();
            }
        } catch (SQLException e) {
            String msg = String.format("Unable to close all connections! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
    }

    private Connection createConnection() {
        Connection connection;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_DB_PROPERTIES));
            String dbUrl = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            Class.forName(properties.getProperty("driver.class.name"));
            connection = DriverManager.getConnection(dbUrl, user, password);

            executeTablesSQL(connection); // start db dropTables generating
        } catch (IOException | ClassNotFoundException | SQLException e) {
            String msg = String.format("Unable to create connection! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
        return connection;
    }

    private void executeTablesSQL(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CREATE_TABLES));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) sb.append(line);
            String query = sb.toString();
            statement.executeUpdate(query);
        } catch (SQLException | IOException e) {
            String msg = String.format("Unable to execute new sql tables for DB (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
    }

    public static ConnectionPool getInstance() throws SystemMalfunctionException {
        if (instance == null) instance = new ConnectionPool();
        return instance;
    }
}
