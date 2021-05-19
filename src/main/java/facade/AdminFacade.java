package facade;

import db.dao.*;
import ex.CompanyAlreadyExistException;
import ex.InvalidLoginException;
import lombok.AllArgsConstructor;
import model.Company;
import model.Coupon;

import java.util.Collection;

@AllArgsConstructor
public class AdminFacade extends AbsFacade {

    private CouponDao couponDao;
    private CompanyDao companyDao;
    private CustomerDao customerDao;

    protected static AdminFacade performLogin(String email, String password) throws InvalidLoginException {
        if (email.equals("admin") && password.equals("777"))
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

    public void removeCompany(long id){
        Collection<Coupon> coupons = couponDao.getCouponsByCompanyId(id);
    }
}
