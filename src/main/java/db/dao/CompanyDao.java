package db.dao;

import ex.NoSuchCompanyException;
import model.Company;
import model.Coupon;

import java.util.Collection;

public interface CompanyDao {

    void setCompany(Company company);

    Company getCompany();

    void removeCompany(long id) throws NoSuchCompanyException;

    Company updateCompany(Company company) throws NoSuchCompanyException;

    Coupon createCoupon(Coupon coupon);

    Collection<Coupon> getCompanyCoupons(long id) throws NoSuchCompanyException;

    Collection<Company> getAllCompanies();

    Collection<Company> getAllCompaniesAndCoupons();

    Company getCompanyById(long id);
}
