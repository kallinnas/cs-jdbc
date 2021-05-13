package db;

import db.dao.CompanyDBDao;
import db.dao.CompanyDao;
import db.dao.CouponDBDao;
import db.dao.CouponDao;
import ex.NoSuchCompanyException;
import model.Company;
import model.Coupon;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws NoSuchCompanyException {
        CompanyDao dao = new CompanyDBDao();
        CouponDao daoCoupon = new CouponDBDao();
        Company company = new Company();

        /*CREATE*/
//        company.setId(0);
//        company.setName("Super");
//        company.setImageURL("DUper");
//        dao.createCompany(company);

        //DELETE
//        dao.removeCompany(14);

        //Update
//        company.setId(15);
//        company.setName("High 5");
//        company.setImageURL("fivelogo");
//        dao.updateCompany(company);

        /* Create COUPON */
        Coupon coupon = new Coupon(
                0, 1, "t", LocalDate.now(),
                2.0, "des", "logo");
        Coupon coupon1 = dao.createCoupon(coupon);
        System.out.println(coupon1);
        /*SELECT*/
//        Collection<Coupon> coupons = dao.getCompanyCoupons(1);
//        for (Coupon coupon : coupons) {
//            System.out.println(coupon);
//        }
    }
}
