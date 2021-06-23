package facade;

import model.Coupon;

import java.util.Collection;

public class DisplayDBResult {
    private static int charAmount = 0;
    private final static double spaceForID = 8;
    private final static double spaceForCompanyID = 6;
    private final static double spaceForDate = 10;
    private final static double spaceForPrice = 10;
    private final static double spaceForTitle = 50;
    private final static double spaceForDescription = 50;
    private final static double spaceForImageUrl = 50;
    private final static String tableBorder = "________________________________________________________________" +
            "________________________________________________________________" +
            "________________________________________________________________";
    private final static String tableHeadCoupon = "|   Id   |Com_Id|   Date   |   Price  |" +
            "                       Title                      |" +
            "                   Description                    |" +
            "                     ImageURL                     |";
    //2021-06-13

    public static void showCouponResult(Collection<Coupon> coupons) {
        System.out.println(tableBorder);
        System.out.println(tableHeadCoupon);
        System.out.println(tableBorder);
        for (Coupon coupon : coupons) {
            System.out.print("|");
            columnDigitBuilder(spaceForID, coupon.getId());
            System.out.print("|");
            columnDigitBuilder(spaceForCompanyID, coupon.getCompanyId());
            System.out.print("|");
            columnStringBuilder(spaceForDate, coupon.getStartDate().toString());
            System.out.print("|");
            columnDigitBuilder(spaceForPrice, coupon.getPrice());
            System.out.print("|");
            columnStringBuilder(spaceForTitle, coupon.getTitle());
            System.out.print("|");
            columnStringBuilder(spaceForDescription, coupon.getDescription());
            System.out.print("|");
            columnStringBuilder(spaceForImageUrl, coupon.getImageURL());
            System.out.println("|");
        }
        System.out.println(tableBorder);
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
}
