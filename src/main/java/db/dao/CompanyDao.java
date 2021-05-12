package db.dao;

import ex.NoSuchCompanyException;
import model.Company;

public interface CompanyDao {

    Company createCompany(Company company);

    void removeCompany(long id) throws NoSuchCompanyException;

    Company updateCompany(Company company) throws NoSuchCompanyException;
}
