package facade;

import db.ConnectionPool;
import db.dao.*;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import ex.NoSuchCustomerException;
import facade.ui.AdminMenuUI;
import facade.ui.MenuUI;
import lombok.*;
import model.Company;
import model.Coupon;
import model.Customer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import static facade.ui.MenuUI.readContext;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class AdminFacade extends AbsFacade {

    @Getter
    private final static String LOGIN = "a";
    private final static String PASSWORD = "a";

    @NonNull
    private AdminMenuUI ui;
    @NonNull
    private CouponDao couponDao;
    @NonNull
    private CompanyDao companyDao;
    @NonNull
    private CustomerDao customerDao;
    @NonNull
    private UserDao userDao;

    private boolean isNotRequiredType;

    private Customer customer;
    private Company company;
    private Coupon coupon;

    AbsFacade initFacade(String email, String password) throws InvalidLoginException {
        if (email.equals(LOGIN) && password.equals(PASSWORD)) {
            initThis();
            ui.setFacade(this);
            return this;
        } else
            throw new InvalidLoginException(String.format("Unable to login with email: %s and password %s", email, password));
    }

    private void initThis() {
        this.ui = new AdminMenuUI();
        this.couponDao = new CouponDBDao();
        this.companyDao = new CompanyDBDao();
        this.customerDao = new CustomerDBDao();
        this.userDao = new UserDBDao();
    }

    /* ADMIN */
    public void runAdminFacade() {
        System.out.println("Welcome to Admin control panel!");
        ui.mainMenu();
    }

    public void reloadDB() throws IOException {
        System.out.print("Are you sure that you want to remove all data from DB? Y/N ");
        val userAnswer = readContext();
        switch (userAnswer.toLowerCase()) {
            case "y":
                ConnectionPool.getInstance().reloadDB();
                ConnectionPool.getInstance().executeTablesSQL(ConnectionPool.getInstance().getConnection());
                System.out.println("DB was reloaded successfully! All data destroyed.");
                break;
            case "n":
                System.out.println("DB was not reloaded.");
                break;
            default:
                System.out.println(WRONG_INSERT_MSG);
        }
    }


    /* COMPANY */
    public void removeCompany() {
        isNotRequiredType = true;
        long id = 0;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of company that you want to delete:");
                getExistCompany(id = Long.parseLong(readContext()));
            }
            DisplayDBResult.showCompanyResult(Collections.singleton(company));
            beforeRemoveCompany(id);
        } catch (IOException e) {
            System.out.print(WRONG_INSERT_MSG);
            removeCompany();
        }
        closeMenu();
        ui.companyMenu();
    }

    public void updateCompany() {
        long id = 0;
        try {
            System.out.print("Enter id # of company for an update, to get back press just enter:");
            val s = readContext();
            if (!s.equals("")) {
                id = Long.parseLong(s);
                company = companyDao.getCompanyById(id);
                if (company != null) {
                    System.out.print("Update name company: ");
                    company.setName(readContext());
                    System.out.print("Update logo-url: ");
                    company.setImageURL(readContext());
                    company = companyDao.updateCompany(company);
                    System.out.println("Company name " + company.getName() +
                            " was just set successfully!");
                }
            } else ui.companyMenu();
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            updateCompany();
        } catch (NoSuchCompanyException e) {
            System.out.println(String.format(NO_COMPANY_ID, id));
            updateCompany();
        }
    }


    /* COUPON */
    public void updateCoupon() {
        isNotRequiredType = true;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to update:");
                val id = Long.parseLong(readContext());
                getExistCoupon(id);
            }
            System.out.print("Update coupon title from " + coupon.getTitle() + " to:");
            coupon.setTitle(readContext());
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Update coupon price from " + coupon.getPrice() + " to:");
                double price = Double.parseDouble(readContext());
                coupon.setPrice(price);
                isNotRequiredType = false;
            }
            System.out.print("Update coupon description " + coupon.getDescription() + " to:");
            coupon.setDescription(readContext());
            System.out.print("Update coupon image from " + coupon.getImageURL() + " to:");
            coupon.setImageURL(readContext());
        } catch (IOException e) {
            System.out.print(WRONG_INSERT_MSG);
            updateCoupon();
        }
        DisplayDBResult.showCouponsResult(Collections
                .singleton(couponDao.updateCoupon(coupon)));
        AbsFacade.closeMenu();
        ui.couponMenu();
    }

    public void removeCoupon() {
        isNotRequiredType = true;
        long id = 0L;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to delete:");
                getExistCoupon(id = Long.parseLong(readContext()));
            }
            DisplayDBResult.showCouponsResult(Collections.singleton(coupon));
            beforeRemoveCoupon(id);
        } catch (IOException | SQLException e) {
            System.out.print(WRONG_INSERT_MSG);
            removeCoupon();
        }
        closeMenu();
        ui.couponMenu();
    }

    public void sendCoupon() {
        AtomicLong ownerId = new AtomicLong(0L);
        try {
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Enter coupon id that you want to send: ");
                getExistCoupon(Long.parseLong(readContext()));
            }
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Enter customer id to send a coupon: ");
                ownerId.set(Long.parseLong(readContext()));
                if (customerDao.getCustomerById(ownerId.get()) != null) isNotRequiredType = false;
            }
            couponDao.getCouponsByCustomerId(ownerId.get()).stream()
                    .filter(c -> c.getId() == coupon.getId())
                    .findAny().ifPresentOrElse((value) -> {
                        System.out.println(String.format(CUSTOMER_HAS_COUPON, ownerId.get(), coupon.getId()));
                        ui.couponMenu();
                    },
                    () -> couponDao.purchaseCoupon(ownerId.get(), coupon.getId()));
        } catch (NoSuchCustomerException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        System.out.println(String.format(SUCCESS_SENT, coupon.getId(), coupon.getTitle(), ownerId.get()));
        ui.couponMenu();
    }


    /* CUSTOMER */
    public void getAllCustomers() {
        DisplayDBResult.showCustomerResult(customerDao.getAllCustomers());
        closeMenu();
    }

    public void searchCustomerById() {
        while (true) {
            long id = 0;
            System.out.print("Enter customer id:");
            try {
                id = Long.parseLong(readContext());
                DisplayDBResult.showCustomerResult(Collections
                        .singleton(new CustomerDBDao().getCustomerById(id)));
                AbsFacade.closeMenu();
                break;
            } catch (IOException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCustomerException e) {
                System.out.println(String.format(AbsFacade.NO_CUSTOMER_ID, id));
            }
        }
        ui.searchCustomerMenu();
    }

    public void searchCustomerByFirstName() {
        while (true) {
            String name = "";
            System.out.print("Enter customer's first name:");
            try {
                name = MenuUI.readContext();
                val customers = new CustomerDBDao().getCustomerByFirstName(name);
                if (customers.isEmpty()) {
                    throw new NoSuchCustomerException(String.format(AbsFacade.NO_COMPANY_NAME, name));
                }
                DisplayDBResult.showCustomerResult(customers);
                AbsFacade.closeMenu();
                break;
            } catch (IOException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCustomerException e) {
                System.out.println(e.getMessage());
            }
        }
        ui.searchCustomerMenu();
    }

    public void searchCustomerByLastName() {
        while (true) {
            String name = "";
            System.out.print("Enter customer's last name:");
            try {
                name = MenuUI.readContext();
                val customers = new CustomerDBDao().getCustomerByLastName(name);
                if (customers.isEmpty()) {
                    throw new NoSuchCustomerException(String.format(AbsFacade.NO_COMPANY_NAME, name));
                }
                DisplayDBResult.showCustomerResult(customers);
                AbsFacade.closeMenu();
                break;
            } catch (IOException e) {
                System.out.println(AbsFacade.WRONG_INSERT_MSG);
            } catch (NoSuchCustomerException e) {
                System.out.println(String.format(AbsFacade.NO_COMPANY_NAME, name));
            }
        }
        ui.searchCustomerMenu();
    }

    public void updateCustomer() {
        long id = 0;
        try {
            System.out.print("Enter id # of customer for an update, to get back press just enter:");
            val s = readContext();
            if (!s.equals("")) {
                id = Long.parseLong(s);
                customer = customerDao.getCustomerById(id);
                if (customer != null) {
                    System.out.print("Update customer's first name " + customer.getFirstName() + " to: ");
                    customer.setFirstName(readContext());
                    System.out.print("Update customer's last name " + customer.getLastName() + " to: ");
                    customer.setLastName(readContext());
                    customer = customerDao.updateCustomer(customer);
                    System.out.println("Customer first name " + customer.getFirstName() +
                            " was just set successfully!");
                }
            } else ui.customerMenu();
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            updateCustomer();
        } catch (NoSuchCustomerException e) {
            System.out.println(String.format(NO_CUSTOMER_ID, id));
            updateCustomer();
        }
    }

    public void removeCustomer() {
        isNotRequiredType = true;
        long id = 0;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of customer that you want to delete:");
                getExistCustomer(id = Long.parseLong(readContext()));
            }
            DisplayDBResult.showCustomerResult(Collections.singleton(customer));
            beforeRemoveCustomer(id);
        } catch (IOException e) {
            System.out.print(WRONG_INSERT_MSG);
            removeCompany();
        }
        closeMenu();
        ui.customerMenu();
    }


    // Takes off the load from update and remove Coupon methods
    private void getExistCoupon(long id) {
        couponDao.getOptCouponById(id)
                .ifPresentOrElse((value) -> {
                    coupon = value;
                    isNotRequiredType = false;
                }, () -> System.out.println(String.format(NO_COUPON, id)));
    }

    private void getExistCompany(long id) {
        companyDao.getOptCompanyById(id)
                .ifPresentOrElse((value) -> {
                    company = value;
                    isNotRequiredType = false;
                }, () -> System.out.println(String.format(NO_COMPANY_ID, id)));
    }

    private void getExistCustomer(long id) {
        customerDao.getOptCustomerById(id)
                .ifPresentOrElse((value) -> {
                    customer = value;
                    isNotRequiredType = false;
                }, () -> System.out.println(String.format(NO_CUSTOMER_ID, id)));
    }

    private void beforeRemoveCoupon(long id) throws IOException, SQLException {
        System.out.print("Are you sure that you want to delete this coupon? Y/N ");
        val userAnswer = readContext();
        switch (userAnswer.toLowerCase()) {
            case "y":
                couponDao.removeCoupon(id);
                System.out.println("Coupon was removed successfully!");
                break;
            case "n":
                System.out.println("Coupon was not removed.");
                break;
            default:
                System.out.println(WRONG_INSERT_MSG);
                beforeRemoveCoupon(id);
        }
    }

    private void beforeRemoveCompany(long id) throws IOException {
        System.out.print("Are you sure that you want to delete this company? Y/N ");
        val userAnswer = readContext();
        switch (userAnswer.toLowerCase()) {
            case "y":
                companyDao.removeCompany(id);
                System.out.println("Company was removed successfully!");
                break;
            case "n":
                System.out.println("Company was not removed.");
                break;
            default:
                System.out.println(WRONG_INSERT_MSG);
                beforeRemoveCompany(id);
        }
    }

    private void beforeRemoveCustomer(long id) throws IOException {
        System.out.print("Are you sure that you want to delete this customer? Y/N ");
        val userAnswer = readContext();
        switch (userAnswer.toLowerCase()) {
            case "y":
                customerDao.removeCustomer(id);
                System.out.println("Customer was removed successfully!");
                break;
            case "n":
                System.out.println("Customer was not removed.");
                break;
            default:
                System.out.println(WRONG_INSERT_MSG);
                beforeRemoveCustomer(id);
        }
    }

}
