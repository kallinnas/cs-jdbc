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
    private static final String COL_NAME = "name";
    private static final String COL_COMPANY_ID = "company_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DATE = "startDate";
    private static final String COL_PRICE = "price";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_IMAGE_URL = "image_url";

    /* COMPANY */
    public static final String INSERT_COMPANY = "INSERT INTO " + TABLE_NAME_COMPANY
            + " (" + COL_NAME + "," + COL_IMAGE_URL + ") VALUES(?,?)";

    public static final String UPDATE_COMPANY = "UPDATE " + TABLE_NAME_COMPANY
            + " SET " + COL_NAME + "=?," + COL_IMAGE_URL + "=? WHERE " + COL_ID + "=?";

    public static final String SELECT_COMPANY_COUPONS = "SELECT * FROM " + TABLE_NAME_COUPON
            + " WHERE " + COL_COMPANY_ID + "=?";

    public static final String DELETE_COMPANY = "DELETE FROM " + TABLE_NAME_COMPANY + " WHERE " + COL_ID + "=?";

    public static final String CREATE_COUPON = "INSERT INTO " + TABLE_NAME_COUPON
            + " (" + COL_COMPANY_ID + "," + COL_TITLE + "," + COL_DATE + ","
            + COL_PRICE + "," + COL_DESCRIPTION + "," + COL_IMAGE_URL
            + ") VALUES(?,?,?,?,?,?)";
    public static final String CC = "INSERT INTO coupon" +
            "(company_id,title,startDate, price, description,image_url) VALUES(?,?,?,?,?,?)";

    public static final String SELECT_COUPON_BY_TITLE = "SELECT * FROM " + TABLE_NAME_COUPON
            + " WHERE " + COL_TITLE + "=?";
}
