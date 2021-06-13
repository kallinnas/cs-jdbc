package ui;

import ex.InvalidLoginException;
import facade.AbsFacade;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import model.LoginType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginUI {

    private static String email;
    private static String password;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";
    private static final String LOG_OR_REG_MSG = "To \033[32midentify\033[0m yourself " +
            "press \033[32m1\033[0m, for new \033[32mregistration\033[0m " +
            "press \033[32m2\033[0m  or" +
            " press \033[32m3\033[0m to \033[32mQuit\033[0m than press enter: ";

    private static void registerUser() {

    }

    static void authorisationUser() {
        System.out.print(LOG_OR_REG_MSG);
        try {
            switch (RunUI.userCommandNumber()) {
                case 1:
                    authenticationUserEmailAndPassword();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    RunUI.appRunning = false;
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println(WRONG_INSERT_MSG);
            authorisationUser();
        }
        if (RunUI.adminFacade != null || CompanyUI.companyFacade != null
                || RunUI.customerFacade != null) {
            RunUI.loginIn = true;
        }
    }

    private static void authenticationUserEmailAndPassword() {
        try {
            System.out.print("Email: ");
            email = reader.readLine();
            System.out.print("Password: ");
            password = reader.readLine();

            if (email.equals("admin")) {
                RunUI.adminFacade = (AdminFacade) AbsFacade.login(email, password, LoginType.ADMIN);
            } else {
                RunUI.type = AbsFacade.userRole(email);
                switch (RunUI.type) {
                    case COMPANY:
                        CompanyUI.companyFacade = (CompanyFacade) AbsFacade.login(email, password, LoginType.COMPANY);
                        break;
                    case CUSTOMER:
                        RunUI.customerFacade = (CustomerFacade) AbsFacade.login(email, password, LoginType.CUSTOMER);
                    case GUEST:
                        System.out.println("No user with such email address: " + email + ". Try again or registrate yourself.");
                }
            }
        } catch (IOException | InvalidLoginException e) {
            System.out.println("Unable to authenticate user with such email and password");
            authenticationUserEmailAndPassword();
        }
    }
}
