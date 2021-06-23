package facade.ui;

import ex.InvalidLoginException;
import ex.UserAlreadyExistException;
import facade.AbsFacade;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CustomerFacade;
import lombok.Data;
import model.LoginType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class MenuUIController {
    private static String email;
    private static String password;
    private static LoginType type;

    private MenuUI menuUI;
    private CompanyMenuUI companyMenuUI;
    private CustomerMenuUI customerMenuUI;
    private AdminMenuUI adminMenuUI;

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static boolean appRunning = false;
    static boolean loginIn = false;

    private static final String WELCOME_MSG = "Hello and Welcome to \033[32mCOUPON SYSTEM DB\033[0m!";
    private static final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";
    private static final String LOG_OR_REG_MSG = "To \033[32midentify\033[0m yourself press " +
            "\033[32m1\033[0m, for new " +
            "\033[32mregistration\033[0m press " +
            "\033[32m2\033[0m  or press " +
            "\033[32m3\033[0m to " +
            "\033[32mQuit\033[0m than press enter: ";
    private static final String REGISTER = "To \033[32mregistrate\033[0m as a " +
            "\033[32mcustomer\033[0m press - " +
            "\033[32m1\033[0m or as a " +
            "\033[32mcompany\033[0m press - " +
            "\033[32m2\033[0m and than enter: ";

    public void run() {
        appRunning = true;
        System.out.println(WELCOME_MSG);

        while (appRunning) {
            if (!loginIn) {
                authorisationUser();
            }
        }

    }

    private void registrationUser() {
        System.out.print(REGISTER);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    System.out.print("Registrate your email address: ");
                    email = reader.readLine();
                    System.out.print("Create your password:");
                    password= reader.readLine();
                    AbsFacade.registerUser(email, password, LoginType.CUSTOMER);
                    initGuestMenuUI(email, password);
                    break;
                case 2:
                    System.out.print("Registrate company email address: ");
                    email = reader.readLine();
                    System.out.print("Create your password: ");
                    password= reader.readLine();
                    AbsFacade.registerUser(email, password, LoginType.COMPANY);;
                    initGuestMenuUI(email, password);
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | InvalidLoginException | IOException e) {
            System.out.println(WRONG_INSERT_MSG + e.getMessage());
        } catch (UserAlreadyExistException e) {
            System.out.println("User with such email *" + email +"* already exist!" + e.getMessage());
        }
    }

    void logout() {
        loginIn = false;
        type = null;
        closeAllMenus();
        authorisationUser();
    }

    private void closeAllMenus() {
        companyMenuUI = null;
        customerMenuUI = null;
        adminMenuUI = null;
    }
// check!
    static void stopApp() {
        appRunning = false;
        System.exit(0);
    }


    private void authorisationUser() {
        System.out.print(LOG_OR_REG_MSG);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    System.out.print("Email: ");
                    email = reader.readLine();
                    System.out.print("Password: ");
                    initGuestMenuUI(email, reader.readLine());
                    break;
                case 2:
                    registrationUser();
                    break;
                case 3:
                    MenuUIController.stopApp();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            authorisationUser();
        }
    }

    public void initGuestMenuUI(String email, String password) {
        try {
            type = new CustomerFacade().userRole(email);
            switch (type) {
                case COMPANY:
                    companyMenuUI = new CompanyMenuUI();
                    companyMenuUI.setFacade((CompanyFacade) AbsFacade.login(email, password, LoginType.COMPANY));
                    companyMenuUI.facade.runCompanyFacade();
                    break;
                case CUSTOMER:
                    customerMenuUI = new CustomerMenuUI();
                    customerMenuUI.setFacade((CustomerFacade) AbsFacade.login(email, password, LoginType.CUSTOMER));
                    customerMenuUI.facade.runCustomerFacade();
                    break;
                case ADMIN:
                    adminMenuUI.setFacade((AdminFacade) AbsFacade.login(email, password, LoginType.ADMIN));
                    adminMenuUI.facade.runAdminFacade();
                    break;
                default:
                    System.out.println("No user with such email address: " + email + ". Try again or registrate yourself.");
            }
        } catch (InvalidLoginException e) {
            System.out.println("Unable to authenticate user with such email and password");
            authorisationUser();
        }
    }

}
