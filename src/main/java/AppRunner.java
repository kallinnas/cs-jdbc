import db.StoredProceduresLoaderDB;
import facade.ui.MenuUIController;

public class AppRunner {

    public static void main(String[] args) {
        StoredProceduresLoaderDB.storeProceduresIntoDB();
        MenuUIController controller = new MenuUIController();
        controller.run();

    }
}
