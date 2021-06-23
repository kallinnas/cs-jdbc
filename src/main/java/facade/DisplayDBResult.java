package facade;

import db.dao.CustomerDBDao;
import ex.NoSuchCustomerException;
import model.Company;
import model.Coupon;
import model.Customer;
import model.User;

import java.util.Collection;

public class DisplayDBResult {
    private static int charAmount = 0;
    private final static double SPACE_FOR_ID = 8;
    private final static double SPACE_FOR_COMPANY_ID = 6;
    private final static double SPACE_FOR_DATE = 10;
    private final static double SPACE_FOR_PRICE = 10;
    private final static double SPACE_FOR_TITLE = 50;
    private final static double SPACE_FOR_DESCRIPTION = 50;
    private final static double SPACE_FOR_IMAGE_URL = 50;
    private final static String TABLE_BORDER = "________________________________________________________________" +
            "________________________________________________________________" +
            "________________________________________________________________";
    private final static String TABLE_HEAD_COUPON = "|   Id   |Com_Id|   Date   |   Price  |" +
            "                       Title                      |" +
            "                   Description                    |" +
            "                     ImageURL                     |";
    private final static String TABLE_HEAD_COMPANY = "|   Id   |"+
            "                   Company Name                   |" +
            "                                                           " +
            "Company ImageURL                                                       |";

    public static void showCouponResult(Collection<Coupon> coupons) {
        System.out.println(TABLE_BORDER);
        System.out.println(TABLE_HEAD_COUPON);
        System.out.println(TABLE_BORDER);
        for (Coupon coupon : coupons) {
            System.out.print("|");
            columnDigitBuilder(SPACE_FOR_ID, coupon.getId());
            System.out.print("|");
            columnDigitBuilder(SPACE_FOR_COMPANY_ID, coupon.getCompanyId());
            System.out.print("|");
            columnStringBuilder(SPACE_FOR_DATE, coupon.getStartDate().toString());
            System.out.print("|");
            columnDigitBuilder(SPACE_FOR_PRICE, coupon.getPrice());
            System.out.print("|");
            columnStringBuilder(SPACE_FOR_TITLE, coupon.getTitle());
            System.out.print("|");
            columnStringBuilder(SPACE_FOR_DESCRIPTION, coupon.getDescription());
            System.out.print("|");
            columnStringBuilder(SPACE_FOR_IMAGE_URL, coupon.getImageURL());
            System.out.println("|");
        }
        System.out.println(TABLE_BORDER);
    }

    private static void columnStringBuilder(double spaceForContext, String context) {
        countCharAmount(context);
        if (charAmount > spaceForContext) {
            context = context.substring(0, (int) spaceForContext);
            System.out.print(context);
        } else if (charAmount == spaceForContext) {
            System.out.print(context);
        } else {
            buildColumnWithSpace(spaceForContext, context);
        }
    }

    private static void buildColumnWithSpace(double spaceForContext, String context) {
        countCharAmount(context);
        double counter = spaceForContext - charAmount;
        for (; counter > 0; counter--) {
            counter /= 2;
            if (counter % 1 == 0) {
                printOutSpace(counter);
                System.out.print(context);
                printOutSpace(counter);
                counter = 0;
            } else {
                int freeSpace = (int) (spaceForContext - charAmount);
                int space1 = freeSpace / 2;
                int space2 = freeSpace - space1;
                printOutSpace(space2);
                System.out.print(context);
                printOutSpace(space1);
                counter = 0;
            }
        }
    }

    private static void columnDigitBuilder(double spaceForContext, String context) {
        countCharAmount(context);
        buildColumnWithSpace(spaceForContext, context);
    }

    private static void columnDigitBuilder(double spaceForContext, double context) {
        countCharAmount(String.valueOf(context));
        buildColumnWithSpace(spaceForContext, String.valueOf(context));
    }

    private static void columnDigitBuilder(double spaceForContext, long context) {
        countDigitAmount(context);
        buildColumnWithSpace(spaceForContext, String.valueOf(context));
    }

    private static void printOutSpace(double counter) {
        for (; counter > 0; counter--) System.out.print(" ");
    }

    private static void countDigitAmount(long id) {
        for (; id != 0; charAmount++) id /= 10;
    }

    private static void countCharAmount(String context) {
        charAmount = (int) context.chars().count();
    }

    public static void showCompanyResult(Collection<Company> companies) {
        System.out.println(TABLE_BORDER);
        System.out.println(TABLE_HEAD_COMPANY);
        System.out.println(TABLE_BORDER);
        for (Company company : companies) {
            System.out.print("|");
            columnDigitBuilder(SPACE_FOR_ID, company.getId());
            System.out.print("|");
            columnStringBuilder(SPACE_FOR_DESCRIPTION, company.getName());
            System.out.print("|");
            columnStringBuilder(130, company.getImageURL());
            System.out.println("|");
        }
        System.out.println(TABLE_BORDER);
    }


    public static void showMyAccount(User user) {
        Customer customer = null;
        try {
            customer = new CustomerDBDao().getCustomerById(user.getClient().getId());
        } catch (NoSuchCustomerException e) {
            // ignore
        }
        System.out.println("\033[32mUser ID:\033[0m " + user.getId() + "    \033[32mEmail:\033[0m " + user.getEmail());
        System.out.println("\033[32mCustomer ID:\033[0m " + customer.getId() +
                "   \033[32mFirst Name:\033[0m " + customer.getFirstName() +
                "   \033[32mLast Name:\033[0m " + customer.getLastName());

    }
}
