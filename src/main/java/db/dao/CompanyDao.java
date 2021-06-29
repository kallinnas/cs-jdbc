package db.dao;

import ex.NoSuchCompanyException;
import model.Company;
import model.Coupon;

import java.util.Collection;
import java.util.Optional;

public interface CompanyDao {

    void setCompany(Company company);

    Company getCompany();

    void removeCompany(long id);

    Company updateCompany(Company company) throws NoSuchCompanyException;

    Coupon createCoupon(Coupon coupon);

    Collection<Coupon> getCompanyCoupons(long id) throws NoSuchCompanyException;

    Collection<Company> getAllCompanies();

    Collection<Company> getAllCompaniesAndCoupons();

    Company getCompanyById(long id) throws NoSuchCompanyException;

    Collection<Company> getCompanyByName(String name) throws NoSuchCompanyException;

    Optional<Company> getOptCompanyById(long id);
}
