package db.dao;

import ex.NoSuchCustomerException;
import model.Customer;

import java.util.Collection;
import java.util.Optional;

public interface CustomerDao {
    Customer getCustomer();

    Customer updateCustomer(Customer customer) throws NoSuchCustomerException;

    Customer getCustomerById(long id) throws NoSuchCustomerException;

    Optional<Customer> getOptCustomerById(long id);

    void setCustomer(Customer customer);

    Collection<Customer> getAllCustomers();

    Collection<Customer> getCustomerByFirstName(String name) throws NoSuchCustomerException;

    Collection<Customer> getCustomerByLastName(String name);

    void removeCustomer(long id);
}
