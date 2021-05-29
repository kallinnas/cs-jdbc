package db;

public class Schema {
    /* Tables */
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_CUSTOMER = "customer";
    private static final String TABLE_NAME_COMPANY = "company";
    private static final String TABLE_NAME_COUPON = "coupon";
    private static final String TABLE_NAME_CUSTOMER_COUPON = "customer_coupon";

    /* Columns */
    private static final String COL_ID = "id";
    private static final String COL_CLIENT_ID = "client_id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private static final String COL_NAME = "name";
    private static final String COL_COMPANY_ID = "company_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DATE = "startDate";
    private static final String COL_PRICE = "price";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_IMAGE_URL = "image_url";

    /* Procedure names */
    private static final String DROP_IF = "DROP PROCEDURE IF EXISTS";

    /* COMPANY */
    public static final String INSERT_COMPANY = "INSERT INTO " +
            TABLE_NAME_COMPANY + " (" +
            COL_NAME + "," +
            COL_IMAGE_URL + ") VALUES(?,?)";

    public static final String UPDATE_COMPANY = "UPDATE " +
            TABLE_NAME_COMPANY + " SET " +
            COL_NAME + "=?," +
            COL_IMAGE_URL + "=? WHERE " +
            COL_ID + "=?";

    public static final String SELECT_COMPANY_COUPONS = "SELECT * FROM " +
            TABLE_NAME_COUPON + " WHERE " +
            COL_COMPANY_ID + "=?";

    public static final String DELETE_COMPANY = "DELETE FROM " +
            TABLE_NAME_COMPANY + " WHERE " +
            COL_ID + "=?";

    public static final String CREATE_COUPON = "INSERT INTO " +
            TABLE_NAME_COUPON + " (" + COL_COMPANY_ID + "," +
            COL_TITLE + "," + COL_DATE + "," +
            COL_PRICE + "," + COL_DESCRIPTION + "," +
            COL_IMAGE_URL + ") VALUES(?,?,?,?,?,?)";

    public static final String SELECT_COUPON_BY_TITLE = "SELECT * FROM " +
            TABLE_NAME_COUPON + " WHERE " +
            COL_TITLE + "=?";

    /* COUPON */
    public static final String DELETE_COUPON = "DELETE FROM " +
            TABLE_NAME_COUPON + " WHERE " + COL_ID + "=?";

    /* USER */
    public static final String SELECT_USER_BY_EMAIL = "SELECT * FROM " +
            TABLE_NAME_USER + " WHERE " + COL_EMAIL + "=?";

    public static final String INSERT_USER = "INSERT INTO " +
            TABLE_NAME_USER + " (" + COL_EMAIL + "," +
            COL_PASSWORD + "," + COL_ROLE + ") VALUES(?,?,?)";

    public static final String INSERT_NEW_COMPANY = "BEGIN;" +
            "INSERT INTO " +
            TABLE_NAME_COMPANY + " (" +
            COL_NAME + "," +
            COL_IMAGE_URL + ") VALUES('','');" +
            "INSERT INTO " + TABLE_NAME_USER + "(" +
            COL_ROLE + "," +
            COL_CLIENT_ID + "," +
            COL_EMAIL + "," +
            COL_PASSWORD + ") VALUES(2,LAST_INSERT_ID(),?,?);"
            + "COMMIT;";


    /* STORED PROCEDURES */
    public static final String GENERATE_PROC_DELETE_COUPON = DROP_IF + " `delete_coupon`; " +
            "CREATE PROCEDURE `delete_coupon`(IN COU_ID INT)" +
            "BEGIN " +
            "DELETE FROM " + TABLE_NAME_COUPON + " WHERE " + COL_ID + "=COU_ID; " +
            "END";

    public static final String GENERATE_PROC_USER_LOGIN = DROP_IF + "`user_login`; " +
            "CREATE PROCEDURE `user_login`(IN EMAIL VARCHAR(255), IN PASSWORD VARCHAR(255))" +
            "BEGIN " +
            " SELECT * FROM " + TABLE_NAME_USER +
            " WHERE " + COL_EMAIL + "=EMAIL" +
            " AND " + COL_PASSWORD + "=PASSWORD; " +
            "END";

    public static final String GENERATE_PROC_GET_ALL_COMPANIES = DROP_IF + " `get_companies`; " +
            "CREATE PROCEDURE `get_companies`() " +
            "BEGIN " +
            " SELECT * FROM " + TABLE_NAME_COMPANY + "; " +
            "END";

    public static final String GENERATE_PROC_GET_ALL_COMPANIES_AND_COUPONS = DROP_IF + " `get_companies_and_coupons`; " +
            "CREATE PROCEDURE `get_companies_and_coupons`() " +
            "BEGIN " +
            " SELECT * FROM " + TABLE_NAME_COUPON +
            " JOIN " + TABLE_NAME_COMPANY +
            " ON " + TABLE_NAME_COUPON +
            "." + COL_COMPANY_ID +
            " = " + TABLE_NAME_COMPANY +
            "." + COL_ID +
            " ORDER BY " + COL_COMPANY_ID + ";" +
            "END";

}
