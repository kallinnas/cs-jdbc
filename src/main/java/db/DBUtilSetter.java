package db;

import model.Client;
import model.Company;
import model.Coupon;
import model.User;

import java.sql.*;
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

    public static Coupon resultSetToCoupon(ResultSet resultRow) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(resultRow.getLong(1));
        coupon.setCompanyId(resultRow.getLong(2));
        coupon.setTitle(resultRow.getString(3));
        coupon.setStartDate(resultRow.getDate(4).toLocalDate());
        coupon.setPrice(resultRow.getDouble(5));
        coupon.setDescription(resultRow.getString(6));
        coupon.setImageURL(resultRow.getString(7));
        return coupon;
    }

    public static Company resultSetToCompany(ResultSet resultRow, int startOrderNum) throws SQLException {
        Company company = new Company();
        company.setId(resultRow.getLong(startOrderNum));
        company.setName(resultRow.getString(++startOrderNum));
        company.setImageURL(resultRow.getString(++startOrderNum));
        return company;
    }

    /* STMT SETTERS */
    public static void applyCouponValuesOnStatement(PreparedStatement preStmt, Coupon coupon) throws SQLException {
        preStmt.setLong(1, coupon.getCompanyId());
        preStmt.setString(2, coupon.getTitle());
        preStmt.setDate(3, Date.valueOf(coupon.getStartDate()));
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
}
