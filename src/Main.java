
import helpers.Helper;
import javax.swing.SwingUtilities;
import views.Login;

/**
 *
 * @author Leonardo Momente
 */
public class Main {

    /**
     * Método principal que inicializa toda a aplicação.
     *
     * @param args
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            Helper.getInstance().switchUI(login);
            login.setVisible(true);
        });

    }
}
