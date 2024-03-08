import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class implement the ActionListener interface,
 * It exits the program when the corresponding button triggered.
 * @author Tam Ngo Hin
 * @version 1.0
 * @since 2023-04-07
 */
public class MenuListener implements ActionListener {

    /**
     * This method is an overridden method to exit the program when playing press the exit button.
     * Calls System.exit(0) to terminate the program.
     * @param e is the action event that triggered the method.
     */
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}