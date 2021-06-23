package facade;

import db.dao.*;
import ex.CompanyAlreadyExistException;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Company;
import model.Coupon;
import model.User;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class AdminFacade extends AbsFacade {

    private final static String LOGIN = "admin";
    private final static String PASSWORD = "777";
    private CouponDao couponDao;
    private CompanyDao companyDao;
    private CustomerDao customerDao;
    private UserDao userDao;

    AdminFacade initFacade(String email, String password) throws InvalidLoginException {
        if (email.equals(LOGIN) && password.equals(PASSWORD))
            return new AdminFacade(new CouponDBDao(), new CompanyDBDao(), new CustomerDBDao(), new UserDBDao());
        else
            throw new InvalidLoginException(String.format("Unable to login with email: %s and password %s", email, password));
    }

    public void runAdminFacade() {

    }

    /* COMPANY */
    public void createCompany(User user) throws CompanyAlreadyExistException {
        if (!userDao.userEmailIsPresent(user.getEmail())){
            userDao.createUserCompany(user.getEmail(), user.getPassword());

        }
    }

    public void removeCompany(long id) throws NoSuchCompanyException, SQLException {
        Collection<Coupon> coupons = couponDao.getCouponsByCompanyId(id);
        for (Coupon coupon : coupons) {
            couponDao.removeCoupon(coupon.getId());
        }
        companyDao.removeCompany(id);
    }

    public void updateCompany(Company company) throws NoSuchCompanyException {
        Optional<Company> any = companyDao.getAllCompanies().stream()
                .filter(c -> c.getId() == company.getId())
                .findAny();
        if (any.isPresent()) companyDao.updateCompany(company);
    }


    public Company getCompanyById(long id) throws NoSuchCompanyException {
        Optional<Company> company = companyDao.getAllCompaniesAndCoupons().stream()
                .filter(c -> c.getId() == id)
                .findAny();
        if (company.isPresent()) return company.get();
        else throw new NoSuchCompanyException("Company with id " + id + " is not exist!");
    }


}
