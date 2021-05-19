package db;

import model.Company;
import model.Coupon;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DBUtilSetter {

    /* CONVERTERS */
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
    public static void applyCouponValuesOnStatement(PreparedStatement stmt, Coupon coupon) throws SQLException {
        stmt.setLong(1, coupon.getCompanyId());
        stmt.setString(2, coupon.getTitle());
        stmt.setDate(3, Date.valueOf(coupon.getStartDate()));
        stmt.setDouble(4, coupon.getPrice());
        stmt.setString(5, coupon.getDescription());
        stmt.setString(6, coupon.getImageURL());
    }

    public static void applyCompanyValuesOnStatement(PreparedStatement stmt, Company company) throws SQLException {
        stmt.setString(1, company.getName());
        stmt.setString(2, company.getImageURL());
    }


}
