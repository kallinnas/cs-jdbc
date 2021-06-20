package db;

import common.StatementUtils;
import common.SystemMalfunctionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StoredProceduresLoaderDB {

    public static void storeProceduresIntoDB() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute(Schema.GENERATE_PROC_GET_ALL_COMPANIES_AND_COUPONS);
            stmt.execute(Schema.GENERATE_PROC_GET_ALL_COMPANIES);
            stmt.execute(Schema.GENERATE_PROC_DELETE_COUPON);
            stmt.execute(Schema.GENERATE_PROC_USER_LOGIN);
            stmt.execute(Schema.GENERATE_INSERT_USER_CUSTOMER);
            stmt.execute(Schema.GENERATE_INSERT_USER_COMPANY);
            stmt.execute(Schema.GENERATE_UPDATE_COUPON);
            stmt.execute(Schema.GENERATE_UPDATE_COUPON_OWNER);
        } catch (SQLException e) {
            throw new SystemMalfunctionException("Unable to upload procedures to DB! " + e.getMessage());
        } finally {
            ConnectionPool.getInstance().putConnection(connection);
            StatementUtils.closeAll(stmt);
        }
    }
}
