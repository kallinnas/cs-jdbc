package ui;

import facade.AdminFacade;
import facade.CustomerFacade;
import model.LoginType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunUI {
    static boolean appRunning = false;
    static boolean loginIn = false;

    static LoginType type;
    static AdminFacade adminFacade;
    static CustomerFacade customerFacade;

    private static final String WELCOME_MSG = "Hello and Welcome to \033[32mCOUPON SYSTEM DB\033[0m!";
    private static final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";

    public static void run() {
        appRunning = true;
        System.out.println(WELCOME_MSG);

        while (appRunning) {
            if (!loginIn) {
                LoginUI.authorisationUser();
            }

            switch (type) {
                case CUSTOMER:
                    useCustomerFacade();
                    break;
                case COMPANY:
                    CompanyUI.useCompanyFacade();
                    break;
                case ADMIN:
                    useAdminFacade();
            }

        }

    }

    private static void useAdminFacade() {
    }



    static int userCommandNumber() {
        int i = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            i = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            System.out.println(WRONG_INSERT_MSG);
        }
        return i;
    }



    private static void useCustomerFacade() {
        System.out.println();
    }


    static void logout() {
        loginIn = false;
        type = null;
        CompanyUI.companyFacade = null;
    }
}
