package db;

import ex.SystemMalfunctionException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ConnectionPool {
    private static ConnectionPool instance;
    private BlockingQueue<Connection> connections;
    private static final int MAX_CONNECTIONS = 1;
    private static final String NAME_OF_PROPERTIES = "src/main/resources/config.properties";
    private static final String CREATE_TABLES = "src/main/resources/createTables";

    private ConnectionPool() throws SystemMalfunctionException {
        this.connections = new LinkedBlockingQueue<>(MAX_CONNECTIONS);
        try {
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                connections.offer(createConnection());
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            String msg = String.format("Unable to instantiate Connection Pool (%s)", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
    }

    Connection getConnection() throws SystemMalfunctionException {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            String msg = String.format("Unable to get Connection! (%s) ", e.getMessage());
            throw new SystemMalfunctionException(msg);
        }
    }

    private Connection createConnection() throws SQLException, IOException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(NAME_OF_PROPERTIES));

        String dbUrl = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        Class.forName(properties.getProperty("driver.class.name"));
        Connection connection = DriverManager.getConnection(dbUrl, user, password);

        executeTablesSQL(connection); // start db tables generating
        return connection;
    }

    private void executeTablesSQL(Connection connection) throws SQLException, IOException {
        Statement statement = connection.createStatement();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(CREATE_TABLES));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) sb.append(line);
        String query = sb.toString();
        statement.executeUpdate(query);
    }

    static ConnectionPool getInstance() throws SystemMalfunctionException {
        if (instance == null) instance = new ConnectionPool();
        return instance;
    }
}
