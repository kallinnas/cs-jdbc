package facade.ui;

import ex.InvalidLoginException;
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
public class LoginController {
    private static String email;
    private static String password;
    private static LoginType type;

    private CompanyMenuUI companyMenuUI;
    private CustomerMenuUI customerMenuUI;
    private AdminMenuUI adminMenuUI;

    private MenuUI menuUI;

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static boolean appRunning = false;
    static boolean loginIn = false;

    private static final String WELCOME_MSG = "Hello and Welcome to \033[32mCOUPON SYSTEM DB\033[0m!";
    private static final String WRONG_INSERT_MSG = "Wrong command number. Try more. ";
    private static final String LOG_OR_REG_MSG = "To \033[32midentify\033[0m yourself " +
            "press \033[32m1\033[0m, for new \033[32mregistration\033[0m " +
            "press \033[32m2\033[0m  or" +
            " press \033[32m3\033[0m to \033[32mQuit\033[0m than press enter: ";

    public void run() {
        appRunning = true;
        System.out.println(WELCOME_MSG);

        while (appRunning) {
            if (!loginIn) {
                authorisationUser();
            }


        }

    }

    void logout() {
        loginIn = false;
        switch (type) {
            case ADMIN:
                adminMenuUI.setFacade(null);
                break;
            case CUSTOMER:
                customerMenuUI.setFacade(null);
                break;
            case COMPANY:
                companyMenuUI.setFacade(null);
                break;
            default:
                System.out.println("Can't make logout!");
        }
        type = null;
    }

    static void stopApp() {
        appRunning = false;
        System.exit(0);
    }


    private void authorisationUser() {
        System.out.print(LOG_OR_REG_MSG);
        try {
            switch (MenuUI.readCommandNumber()) {
                case 1:
                    initLoginController();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    LoginController.stopApp();
                    break;
                default:
                    throw new NumberFormatException();
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println(WRONG_INSERT_MSG);
            authorisationUser();
        }
        if (adminMenuUI.facade != null || companyMenuUI.facade != null
                || customerMenuUI.facade != null) loginIn = true;
    }

    private void initLoginController() {
        try {
            System.out.print("Email: ");
            email = reader.readLine();
            System.out.print("Password: ");
            password = reader.readLine();

            type = new CustomerFacade().userRole(email);
            switch (type) {
                case COMPANY:
                    companyMenuUI = new CompanyMenuUI();
                    companyMenuUI.setFacade((CompanyFacade) AbsFacade.login(email, password, LoginType.COMPANY));
                    companyMenuUI.facade.runCompanyFacade();
                    break;
                case CUSTOMER:
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
        } catch (IOException | InvalidLoginException e) {
            System.out.println("Unable to authenticate user with such email and password");
            initLoginController();
        }
    }

    private static void registerUser() {

    }


}
