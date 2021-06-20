package facade;

import db.dao.*;
import ex.InvalidLoginException;
import ex.NoSuchCompanyException;
import ex.NoSuchCouponException;
import lombok.*;
import model.Company;
import model.Coupon;
import model.User;
import facade.ui.CompanyMenuUI;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@RequiredArgsConstructor
public class CompanyFacade extends AbsFacade {

    private User user;
    private Company company;
    private Coupon coupon;
    @NonNull
    private CompanyMenuUI ui;
    @NonNull
    private CouponDao couponDao;
    @NonNull
    private CompanyDao companyDao;
    private boolean isNotRequiredType;

    AbsFacade initFacade(String email, String password) throws InvalidLoginException {
        user = new UserDBDao().getUserByEmailAndPassword(email, password);
        initThis(new CompanyMenuUI(), new CouponDBDao(), new CompanyDBDao());
        company = companyDao.getCompanyById(user.getClient().getId());
        companyDao.setCompany(company);
        ui.setFacade(this);
        if (user != null) return this;
        else throw new InvalidLoginException("No user with such email " + email + "!");
    }

    private void initThis(CompanyMenuUI ui, CouponDBDao couponDBDao, CompanyDBDao companyDBDao) {
        this.ui = ui;
        this.couponDao = couponDBDao;
        this.companyDao = companyDBDao;
    }

    public void runCompanyFacade() {
        if (company.getName() == null || company.getName().equals("")) updateCompany();
        System.out.println("Welcome to " + company.getName() + " DataBase! ");
        ui.mainMenu();
    }

    public void updateCompany() {
        try {
            System.out.print("Lets set a name for your company: ");
            company.setName(AbsFacade.reader.readLine());
            System.out.print("\nWhat about the logo? Add url: ");
            company.setImageURL(reader.readLine());
            try {
                company = companyDao.updateCompany(company);
                System.out.println("Company name " + company.getName() + " was just set successfully!");
            } catch (NoSuchCompanyException e) {
                // ignored
            }
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
    }

    public void getAllCompanyCoupons() {
        List<Coupon> coupons = couponDao.getCouponsByCompanyId(user.getClient().getId()).stream()
                .sorted(Comparator.comparingLong(Coupon::getId))
                .collect(Collectors.toList());
        DisplayDBResult.showCouponResult(coupons);
        closeMenu();
        ui.couponMenu();
    }

    public void removeCoupon() {
        isNotRequiredType = true;
        long id = 0;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to delete:");
                long idToDelete = Long.parseLong(reader.readLine());
                if (couponDao.getCouponsByCompanyId(company.getId()).stream().noneMatch(c -> c.getId() == idToDelete)) {
                    System.out.println(company.getName() + " has no coupon with such id " + id + ".");
                } else {
                    coupon = couponDao.getCouponsByCompanyId(company.getId()).stream()
                            .filter(c -> c.getId() == idToDelete)
                            .findFirst()
                            .get();
                    isNotRequiredType = false;
                    id = idToDelete;
                }
            }
            DisplayDBResult.showCouponResult(Collections.singleton(coupon));
            beforeRemove(id);
        } catch (IOException | SQLException e) {
            System.out.print("No coupon with such id or wrong insert.");
            removeCoupon();
        }
        closeMenu();
        ui.couponMenu();
    }

    private void beforeRemove(long id) throws IOException, SQLException {
        System.out.print("Are you sure that you want to delete this coupon? Y/N ");
        String userAnswer = reader.readLine();
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
                beforeRemove(id);
        }
    }

    public void updateCoupon() {
        isNotRequiredType = true;
        try {
            while (isNotRequiredType) {
                System.out.print("Insert id of coupon that you want to update:");
                long id = Long.parseLong(reader.readLine());
                if (couponDao.getCouponsByCompanyId(company.getId()).stream().noneMatch(c -> c.getId() == id)) {
                    System.out.println(company.getName() + " has no coupon with such id " + id + ".");
                } else {
                    coupon = couponDao.getCouponsByCompanyId(company.getId()).stream()
                            .filter(c -> c.getId() == id)
                            .findFirst()
                            .get();
                isNotRequiredType = false;
                }
            }
            System.out.print("Update coupon title from " + coupon.getTitle() + " to:");
            coupon.setTitle(reader.readLine());
            isNotRequiredType = true;
            while (isNotRequiredType) {
                System.out.print("Update coupon price from " + coupon.getPrice() + " to:");
                double price = Double.parseDouble(reader.readLine());
                coupon.setPrice(price);
                isNotRequiredType = false;
            }
            System.out.print("Update coupon description " + coupon.getDescription() + " to:");
            coupon.setDescription(reader.readLine());
            System.out.print("Update coupon image from " + coupon.getImageURL() + " to:");
            coupon.setImageURL(reader.readLine());
        } catch (IOException e) {
            System.out.print("No coupon with such id or wrong insert.");
            updateCoupon();
        }
        DisplayDBResult.showCouponResult(Collections.singleton(couponDao.updateCoupon(coupon)));
        closeMenu();
        ui.couponMenu();
    }

    public void createCompanyCoupon() {
        isNotRequiredType = true;
        coupon = new Coupon();
        coupon.setCompanyId(user.getClient().getId());
        try {
            System.out.println("Coupon title: ");
            coupon.setTitle(reader.readLine());
            while (isNotRequiredType) {
                System.out.println("Coupon price(double): ");
                double price = Double.parseDouble(reader.readLine());
                coupon.setPrice(price);
                isNotRequiredType = false;
            }
            System.out.println("Coupon description: ");
            coupon.setDescription(reader.readLine());
            System.out.println("Coupon image: ");
            coupon.setImageURL(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        Coupon c = couponDao.createCoupon(coupon);
        System.out.println("New coupon #" + c.getId() + " " + c.getTitle() + " was created successfully!");
        closeMenu();
        ui.couponMenu();
    }

    public void searchCouponByTitle() {
        System.out.println("Enter coupon title:");
        String title = "";
        try {
            coupon = couponDao.getCouponByTitle(title = reader.readLine());
            DisplayDBResult.showCouponResult(Collections.singletonList(coupon));
            closeMenu();
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        } catch (NoSuchCouponException e) {
            System.out.println("No coupon with such title: " + title);
            ui.searchCouponMenu();
        }
        ui.searchCouponMenu();
    }

    public void searchCouponById() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            long id = 0;
            System.out.print("Enter coupon id:");
            try {
                id = Long.parseLong(reader.readLine());
                isNotRequiredType = false;
                coupon = couponDao.getCouponById(id);
                DisplayDBResult.showCouponResult(Collections.singleton(coupon));
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            } catch (NoSuchCouponException e) {
                System.out.println("There is no coupon with such id: " + id);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsStartFromDate() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter start from date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsStartFromDate(startDate);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsStartBeforeDate() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter start before date as yyyy-mm-dd:");
            try {
                LocalDate startDate = LocalDate.parse(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsStartBeforeDate(startDate);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | DateTimeException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsByPriceLessThan() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsByPriceLessThan(price);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
            }
        }
        ui.searchCouponMenu();
    }

    public void searchCouponsByPriceMoreThan() {
        isNotRequiredType = true;
        while (isNotRequiredType) {
            System.out.print("Enter price:");
            try {
                double price = Double.parseDouble(reader.readLine());
                isNotRequiredType = false;
                Collection<Coupon> coupons = couponDao.getCouponsByPriceMoreThan(price);
                DisplayDBResult.showCouponResult(coupons);
                closeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println(WRONG_INSERT_MSG);
                ui.searchCouponMenu();
            }
        }
        ui.searchCouponMenu();
    }


}

