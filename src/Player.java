import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.lang.String;
import java.net.Socket;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.BorderFactory; import java.awt.Font; import javax.swing.JButton; import javax.swing.JFrame;
import javax.swing.JLabel; import javax.swing.JPanel; import javax.swing.JTextField;
import javax.swing.border.Border;
import static java.lang.Math.abs;

/**
 * The class implement the Runnable interface,
 * This class is the player class which build the GUI and determine what to display on the board.
 * Variable sock is player sock, name save the name of player, ready check whether both player entered the name and ready to start and moved save the moving position of player.
 * @author Tam Ngo Hin
 * @version 1.0
 * @since 2023-05-02
 */
public class Player implements Runnable {
    private Socket sock;
    private PrintWriter writer;
    private boolean started= false;
    private String name;
    private boolean turn;
    private boolean ready ;
    private boolean player1 = false;
    private boolean player2 = false;
    private ArrayList<Integer> moved = new ArrayList<>();
    private ArrayList<Integer> abs_moved = new ArrayList<>();

    static int count =0;

    /**
     * This is the main method which makes use of go method.
     * @param args Unused.
     * @exception IOException
     * @see IOException
     */
    public static void main(String[] args) throws IOException {
        Player t = new Player();
        t.go();
    }


    /**
     * This is the method which build the GUI and sending the move to the server and receive message from server.
     */
    public void go() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Control");
        JMenuItem menuItem = new JMenuItem("Exit");
        menu.add(menuItem);
        menuItem.addActionListener(new MenuListener());

        JMenu help = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("Instruction");
        help.add(helpItem);
        helpItem.addActionListener(new HelpListener());

        menuBar.add(menu);
        menuBar.add(help);

        JLabel top_Label = new JLabel();
        top_Label.setText("Enter your player name....");

        JFrame frame = new JFrame("Tic Tac Toe");

        JTextField txt_name = new JTextField(10);

        JButton btn_submit= new JButton("Submit");

        JPanel namePanel = new JPanel(new GridLayout(1, 2));

        JPanel GamePanel = new JPanel(new GridLayout(3, 3));

        JLabel [] board ={
                new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),

        };
        for (int i =0 ; i<9 ; i++){
            board[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            board[i].setFont(((new Font("TimesRoman", Font.BOLD, 50))));
            board[i].setHorizontalAlignment(SwingConstants.CENTER);
            GamePanel.add(board[i]);

        }

        namePanel.add(txt_name);
        namePanel.add(btn_submit);

        frame.add(top_Label, BorderLayout.NORTH);
        frame.add(GamePanel, BorderLayout.CENTER);
        frame.add(namePanel, BorderLayout.SOUTH);

        frame.setJMenuBar(menuBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tic Tac Toe");
        frame.setSize(500, 500);
        frame.setVisible(true);


        btn_submit.addActionListener(new ActionListener() {
            /**
             * This is the override method which submit the name for player and wait for start.
             * @param e Unused.
             */
            public void actionPerformed(ActionEvent e) {
                name = txt_name.getText();
                top_Label.setText("WELCOME " + name);
                txt_name.setEnabled(false);
                btn_submit.setEnabled(false);
                started = true;
                frame.setTitle("Tic Tac Toe-Player: " + name);
                writer.println("0 " + name + " E");
            }
        });
        for (int i=0 ; i <9 ; i++) {
            int finalI = i;
            board[i].addMouseListener(new MouseListener() {

                /**
                 * This is the override method which will determine the display of mark according to player 1 or 2.
                 * @param e Unused.
                 */
                public void mouseClicked(MouseEvent e) {

                    if (started && ready){
                        if(!abs_moved.contains(finalI) && turn){
                            if (player1){
                                writer.println("1 "+ name+" "+finalI );
                                board[finalI].setForeground(Color.GREEN);
                                board[finalI].setText("X");
                            }
                            else{
                                writer.println("1 "+ name+" "+ (-finalI)  );
                                board[finalI].setForeground(Color.RED);
                                board[finalI].setText("O");
                            }


                            top_Label.setText("Valid move, wait for your opponent.");
                            System.out.println("Moved " + finalI);
                            turn = false;
                        }

                    }

                }
                @Override
                public void mousePressed(MouseEvent e) {                }
                @Override
                public void mouseReleased(MouseEvent e) {                }
                @Override
                public void mouseEntered(MouseEvent e) {                }
                @Override
                public void mouseExited(MouseEvent e) {                }
            });
        }
        try {
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream(), true);

            String command;
            while ((command = reader.readLine()) != null) {
                System.out.println("Number from server: " + command);
                String[] x = command.split(" ");

                if(Integer.parseInt(x[0]) == 10){
                    JOptionPane.showMessageDialog(frame,"Game Ends. One of the players left." );
                    started = false;
                    System.exit(0);
                    return;
                }

                if(Integer.parseInt(x[0]) == 11){
                    if (player1){
                        JOptionPane.showMessageDialog(frame,"Congratulations You Win.");
                        started = false;

                    }else{
                        JOptionPane.showMessageDialog(frame,"You lose." );
                        started = false;

                    }
                    return;
                }
                if(Integer.parseInt(x[0]) == 12){
                    if (player2){
                        JOptionPane.showMessageDialog(frame,"Congratulations You Win.");
                        started = false;

                    }else{
                        JOptionPane.showMessageDialog(frame,"You lose." );
                        started = false;

                    }
                    return;
                }
                if(Integer.parseInt(x[0]) == 13){
                    JOptionPane.showMessageDialog(frame,"Draw." );
                    started = false;

                    return;
                }

                if (Integer.parseInt(x[0]) == 9){ready = true; }

                if (Integer.parseInt(x[0]) !=9) {

                    if (Integer.parseInt(x[0]) == 0) {
                        if (Integer.parseInt(x[3]) == 1 ) {
                            player1 = true;
                            turn = true;
                        } else {
                            player2 = true;
                            turn = false;
                        }
                    }
                    if(ready){count = Integer.parseInt(x[3]);
                        if (player1) {
                            if(count == 1 || count == 3|| count == 5|| count == 7|| count == 9){
                                top_Label.setText("Your opponent has moved, now is your turn.");

                                turn = true;
                            }
                        }

                        else if (player2) {
                            if(count == 0 || count == 2|| count == 4|| count == 6|| count == 8){
                                top_Label.setText("Your opponent has moved, now is your turn.");

                                turn = true;
                            }
                        }
                        moved.add(Integer.parseInt(x[2]));
                        abs_moved.add(abs(Integer.parseInt(x[2])));

                    }
                    for(Integer a : moved){
                        if(a>0){
                            board[abs(Integer.parseInt(x[2]))].setForeground(Color.green);
                            board[abs(Integer.parseInt(x[2]))].setText("X");
                        }else{
                            board[abs(Integer.parseInt(x[2]))].setForeground(Color.RED);
                            board[abs(Integer.parseInt(x[2]))].setText("O");
                        }
                        if (a == 0&& count%2 ==0 ){
                            board[abs(Integer.parseInt(x[2]))].setForeground(Color.green);
                            board[abs(Integer.parseInt(x[2]))].setText("X");
                        }
                        else if (a == 0 &&(count%2 !=0 || count ==0)){
                            board[abs(Integer.parseInt(x[2]))].setForeground(Color.RED);
                            board[abs(Integer.parseInt(x[2]))].setText("O");
                        }
                    }
                }

            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
        this.go();
    }
}
