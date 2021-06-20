package facade.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public interface MenuUI {

    String WRONG_INSERT_MSG = "Wrong command number. Try more. ";
    String SEARCH_COUPON = "FIND BY:\n1.Id\n2.Title\n3.Coupons start from date\n4.Coupons start before date\n5.Price less than\n6.Price more than\n7.Go back <--";

    void mainMenu();

    void couponMenu();

    void companyMenu();

    void searchCouponMenu();

    void accountMenu();

    void updateMenu();

    static int readCommandNumber() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(reader.readLine());
    }

}
