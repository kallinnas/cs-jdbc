package db;

import db.dao.CompanyDBDao;
import db.dao.CompanyDao;
import db.dao.CouponDBDao;
import db.dao.CouponDao;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import facade.AdminFacade;
import model.Company;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws NoSuchCompanyException, SQLException, InvalidLoginException {
        StoredProceduresLoaderDB.storeProceduresIntoDB();
        CompanyDao daoCompany = new CompanyDBDao();
        CouponDao daoCoupon = new CouponDBDao();
        Company company = new Company();
        AdminFacade a = AdminFacade.performLogin("admin", "777");

        /* delete COMPANY*/
        a.removeCompany(2);

        /*del coupon*/
//        daoCoupon.removeCoupon(4);

        /* getCouponsByCompanyId */
//        Collection<Coupon> couponsByCompanyId = daoCoupon.getCouponsByCompanyId(1);
//        for (Coupon coupon : couponsByCompanyId) {
//            System.out.println(coupon);
//        }

        /* CALLABLE STMT AND STORED PROCEDURE */
//        for (Company company1 : daoCompany.getAllCompaniesAndCoupons()) {
//            System.out.println(company1);
//        }
        /*CREATE*/
//        company.setId(0);
//        company.setName("Super14");
//        company.setImageURL("Opppp");
//        daoCompany.createCompany(company);

        //Update
//        company.setId(15);
//        company.setName("High 5");
//        company.setImageURL("fivelogo");
//        daoCompany.updateCompany(company);

        /* Create COUPON */
//        Coupon coupon = new Coupon(
//                0, 1, "t", LocalDate.now(),
//                2.0, "des", "logo");
//        Coupon coupon1 = dao.createCoupon(coupon);
//        System.out.println(coupon1);

        /*SELECT*/
//        Collection<Coupon> coupons = dao.getCompanyCoupons(1);
//        for (Coupon coupon : coupons) {
//            System.out.println(coupon);
//        }


    }
}
