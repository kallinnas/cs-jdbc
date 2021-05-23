package facade;

import db.dao.*;
import ex.CompanyAlreadyExistException;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Company;
import model.Coupon;

import java.sql.SQLException;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
public class AdminFacade extends AbsFacade {

    private final static String LOGIN = "admin";
    private final static String PASSWORD = "777";
    private CouponDao couponDao;
    private CompanyDao companyDao;
    private CustomerDao customerDao;

    public static AdminFacade performLogin(String email, String password) throws InvalidLoginException {
        if (email.equals(LOGIN) && password.equals(PASSWORD))
            return new AdminFacade(new CouponDBDao(), new CompanyDBDao(), new CustomerDBDao());
        else
            throw new InvalidLoginException(String.format("Unable to login with email: %s and password %s", email, password));
    }

    public void createCompany(Company company) throws CompanyAlreadyExistException {
        for (Company existCompany : companyDao.getAllCompanies()) {
            if (!existCompany.getName().equals(company.getName())) companyDao.createCompany(company);
            else throw new CompanyAlreadyExistException("Company " + company.getName() + " already exist!");
        }
    }

    public void removeCompany(long id) throws NoSuchCompanyException, SQLException {
        Collection<Coupon> coupons = couponDao.getCouponsByCompanyId(id);
        for (Coupon coupon : coupons) {
            couponDao.removeCoupon(coupon.getId());
        }
        companyDao.removeCompany(id);
    }
}
