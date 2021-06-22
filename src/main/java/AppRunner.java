import db.StoredProceduresLoaderDB;
import ex.InvalidLoginException;
import ex.UserAlreadyExistException;
import facade.ui.GuestMenuUI;

import java.io.IOException;

public class AppRunner {

    public static void main(String[] args) throws InvalidLoginException, UserAlreadyExistException, InterruptedException, IOException {
        StoredProceduresLoaderDB.storeProceduresIntoDB();
        GuestMenuUI controller = new GuestMenuUI();
        controller.run();



        /* CREATE USER */
//        facade.registerUser("Serhii@", "abc", LoginType.COMPANY);

        /* LOGIN */
//        facade.login("kallinnas", "1234", LoginType.CUSTOMER);

//        CustomerFacade cusF = null;
//        CompanyFacade comF = null;




        /* get company*/
//        Company c = a.getCompanyById(1);
//        System.out.println(c);
//        c.getCoupons().forEach(System.out::println);

        /* delete/update COMPANY*/
//        Company ass = new Company();
//        ass.setName("onlyMe");
//        ass.setId(1);
//        ass.setImageURL("LML");
//        a.updateCompany(ass);
//        a.removeCompany(2);

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
