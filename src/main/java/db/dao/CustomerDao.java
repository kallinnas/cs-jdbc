package db.dao;

import ex.NoSuchCustomerException;
import model.Customer;

public interface CustomerDao {
    Customer getCustomer();

    Customer updateCustomer(Customer customer) throws NoSuchCustomerException;

    Customer getCustomerById(long id) throws NoSuchCustomerException;

    void setCustomer(Customer customer);
}
