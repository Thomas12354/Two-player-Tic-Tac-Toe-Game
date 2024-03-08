import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The class implement the ActionListener interface,
 * It pop a panel to display message when needed.
 * @author Tam Ngo Hin
 * @version 1.0
 * @since 2023-05-02
 */
public class HelpListener implements ActionListener {

    /**
     * This method is an overridden method to to pop the OptionPane when needed.
     * @param e is the action event that triggered the method.
     */
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,"Some information about the game:\n"
                +"Criteria for a valid move:\n"
                +"-The move is not occupied by any mark.\n"
                +"-The move is made in the player's turn\n"
                +"-The move is made witrhin the 3 x 3 board.\n"
                +"The game would continue and switch among the opposite player " +
                "until it reaches either one of the following conditions:\n"
                +"-Player 1 wins.\n"
                +"-Player 2 wins\n"
                +"Draw."

        );
    }
}