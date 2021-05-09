package db;

import ex.SystemMalfunctionException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ConnectionPool {
    private static ConnectionPool instance;
    private BlockingQueue<Connection> connections;
    private static final int MAX_CONNECTIONS = 5;
    private static final String NAME_OF_PROPERTIES = "config.properties";

    private ConnectionPool() throws SystemMalfunctionException {
        this.connections = new LinkedBlockingQueue<>(MAX_CONNECTIONS);
        try {
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                connections.offer(createConnection());
            }
        } catch (SQLException | IOException e) {
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

    private Connection createConnection() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(ReadProperties());

        String db_url = properties.getProperty("DB_URL");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        return DriverManager.getConnection(db_url, user, password);
    }

    private InputStream ReadProperties() {
        ClassLoader loader = getClass().getClassLoader();
        return loader.getResourceAsStream(NAME_OF_PROPERTIES);
    }

    static ConnectionPool getInstance() throws SystemMalfunctionException {
        if (instance == null) instance = new ConnectionPool();
        return instance;
    }
}
