package db;

import common.SystemMalfunctionException;
import db.dao.CompanyDBDao;
import db.dao.CompanyDao;
import ex.NoSuchCompanyException;
import model.Company;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws NoSuchCompanyException {
        CompanyDao dao = new CompanyDBDao();
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
    }
}
