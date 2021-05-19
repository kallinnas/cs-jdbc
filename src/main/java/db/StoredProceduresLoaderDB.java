package db;

import common.StatementUtils;
import common.SystemMalfunctionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StoredProceduresLoaderDB {

    static void storeProceduresIntoDB() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(Schema.CREATE_PROC_GET_ALL_COMPANIES_AND_COUPONS);
            stmt.execute(Schema.CREATE_PROC_GET_ALL_COMPANIES);
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to get all companies! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
    }
}
