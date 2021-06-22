package db;

import model.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DBUtilSetter {

    /* CONVERTERS */
    public static User resultSetToUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getString(4),
                rs.getString(5), rs.getInt(2));
        user.setId(rs.getLong(1));
        Client client = user.getClient();
        client.setId(rs.getLong(3));
        user.setClient(client);
        return user;
    }

    public static Set<Coupon> resultSetToCouponSet(ResultSet resultRow) throws SQLException {
        Set<Coupon> coupons = new HashSet<>();
        while (resultRow.next()) {
            coupons.add(resultSetToCoupon(resultRow));
        }
        return coupons;
    }

    public static Coupon resultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getLong(1));
        coupon.setCompanyId(rs.getLong(2));
        coupon.setTitle(rs.getString(3));
        coupon.setStartDate(rs.getDate(4).toLocalDate());
        coupon.setPrice(rs.getDouble(5));
        coupon.setDescription(rs.getString(6));
        coupon.setImageURL(rs.getString(7));
        return coupon;
    }

    /* There is method getAllCompaniesAndCoupons where company's columns take different place in resultSet */
    public static Company resultSetToCompany(ResultSet resultRow, int startOrderNum) throws SQLException {
        Company company = new Company();
        company.setId(resultRow.getLong(startOrderNum));
        company.setName(resultRow.getString(++startOrderNum));
        company.setImageURL(resultRow.getString(++startOrderNum));
        return company;
    }

    public static Customer resultSetToCustomer(ResultSet resultRow) throws SQLException {
        Customer customer = new Customer();
        customer.setId(resultRow.getLong(1));
        customer.setFirstName(resultRow.getString(2));
        customer.setLastName(resultRow.getString(3));
        return customer;
    }

    public static long[] resultSetToArray(ResultSet rs) throws SQLException {
        int length = rs.getRow();
        long[] array = new long[length];
        rs.first();
        for (int i = 0; i < length; rs.next()) {
            array[i] = rs.getLong(2);
            i++;
        }
        return array;
    }

    /* STMT SETTERS */
    public static void applyCouponValuesOnStatement(PreparedStatement preStmt, Coupon coupon) throws SQLException {
        preStmt.setLong(1, coupon.getCompanyId());
        preStmt.setString(2, coupon.getTitle());
        preStmt.setDate(3, Date.valueOf(LocalDate.now()));
        preStmt.setDouble(4, coupon.getPrice());
        preStmt.setString(5, coupon.getDescription());
        preStmt.setString(6, coupon.getImageURL());
    }

    public static void applyCompanyValuesOnStatement(PreparedStatement preStmt, Company company) throws SQLException {
        preStmt.setString(1, company.getName());
        preStmt.setString(2, company.getImageURL());
        preStmt.setLong(3, company.getId());
    }

    public static void applyUserValuesOnStmt(PreparedStatement preStmt, String email, String password) throws SQLException {
        preStmt.setString(1, email);
        preStmt.setString(2, password);
    }

    public static void applyCustomerValuesOnStatement(PreparedStatement preStmt, Customer customer) throws SQLException {
        preStmt.setString(1, customer.getFirstName());
        preStmt.setString(2, customer.getLastName());
        preStmt.setLong(3, customer.getId());
    }

    public static void applyUpdatedCouponValuesOnStatement(PreparedStatement preStmt, Coupon coupon) throws SQLException {
        preStmt.setString(1, coupon.getTitle());
        preStmt.setDouble(2, coupon.getPrice());
        preStmt.setString(3, coupon.getDescription());
        preStmt.setString(4, coupon.getImageURL());
        preStmt.setLong(5, coupon.getId());
    }

}
