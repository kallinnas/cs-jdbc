package common;

import java.sql.SQLException;
import java.sql.Statement;

public class StatementUtils {
    public static void closeAll(Statement... statements) {
        for (Statement stmt : statements) {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // Ignore.
                }
        }
    }
}
