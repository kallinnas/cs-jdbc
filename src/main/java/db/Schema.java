package db;

public class Schema {
    /* Tables */
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_CUSTOMER = "customer";
    private static final String TABLE_NAME_COMPANY = "company";
    private static final String TABLE_NAME_COUPON = "coupon";
    /* Columns */
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_IMAGE_URL = "image_url";


    /* COMPANY */
    public static final String INSERT_COMPANY = "INSERT INTO " + TABLE_NAME_COMPANY
            + " (" + COL_NAME + "," + COL_IMAGE_URL + ") VALUE(?,?)";

    public static final String UPDATE_COMPANY = "UPDATE " + TABLE_NAME_COMPANY
            + " SET " + COL_NAME + "=?," + COL_IMAGE_URL + "=? WHERE " + COL_ID + "=?";

    public static final String REMOVE_COMPANY = "DELETE FROM " + TABLE_NAME_COMPANY + " WHERE " + COL_ID + "=?";

}
