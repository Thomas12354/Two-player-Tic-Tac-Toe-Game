import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.Math.abs;


/**
 * This class is for setup the server.
 * This class has the method for build the thread.
 * @author Tam Ngo Hin
 * @version 1.0
 * @since 2023-05-02
 */
public class Server {
    private ServerSocket serverSock;
    private ArrayList<PrintWriter> writers = new ArrayList<>();

    private ArrayList<Integer> player1 = new ArrayList<>();
    private ArrayList<Integer> player2= new ArrayList<>();

    private Socket player1_sock;
    private Socket player2_sock;
    private PrintWriter player1_wri;
    private PrintWriter player2_wri;

    private int count =-2;

    /**
     * This is the main method which makes use of go method.
     * @param args Unused.
     * @exception IOException
     * @see IOException
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.go();
    }

    /**
     * This is the method which build update the server.
     * @exception for player leaving and sending message for player
     */
    public void go() {
        try {
            serverSock = new ServerSocket(5000);
            System.out.println("Server is running...");

            while (true) {
                player1_sock = serverSock.accept();
                System.out.println("Player 1 is connected to client");

                player2_sock= serverSock.accept();
                System.out.println("Player 2 is connected to client");

                ClientHandler clientHandler1 = new ClientHandler(player1_sock,1);
                ClientHandler clientHandler2 = new ClientHandler(player2_sock,2);

                Thread clientThread1 = new Thread(clientHandler1);
                Thread clientThread2 = new Thread(clientHandler2);

                clientThread1.start();
                clientThread2.start();

                clientThread1.join();
                clientThread2.join();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     *The class implement the Runnable interface,
     * This class is the ClientHandler for receiving message from player and sending message to both player.
     * This class has the method for checking the result.
     * @author Tam Ngo Hin
     * @version 1.0
     * @since 2023-05-02
     */
    class ClientHandler implements Runnable{
        private Socket sock;

        private int player_number;

        /**
         * This is a constructor which setup the three properties accordingly.
         * @param sock This is socket of player.
         * @param player_number This is the number of player one or two.
         */
        public ClientHandler(Socket sock, int player_number) {
            this.sock = sock;
            this.player_number =player_number;
        }

        /**
         * This is the method which check whether player one or player two win.
         * @return 1 if player 1 win.
         */
        public int result(){
            if(player1.contains(0) && player1.contains(1) && player1.contains(2)){
                return 1;
            }
            else if(player1.contains(3) && player1.contains(4) && player1.contains(5)){
                return 1;
            }
            else if(player1.contains(6) && player1.contains(7) && player1.contains(8)){
                return 1;
            }
            else if(player1.contains(0) && player1.contains(3) && player1.contains(6)){
                return 1;
            }
            else if(player1.contains(1) && player1.contains(4) && player1.contains(7)){
                return 1;
            }
            else if(player1.contains(2) && player1.contains(5) && player1.contains(8)){
                return 1;
            }
            else if(player1.contains(0) && player1.contains(4) && player1.contains(8)){
                return 1;
            }
            else if(player1.contains(2) && player1.contains(4) && player1.contains(6)){
                return 1;
            }

            else if(player2.contains(0) && player2.contains(1) && player2.contains(2)){
                return 2;
            }
            else if(player2.contains(3) && player2.contains(4) && player2.contains(5)){
                return 2;
            }
            else if(player2.contains(6) && player2.contains(7) && player2.contains(8)){
                return 2;
            }
            else if(player2.contains(0) && player2.contains(3) && player2.contains(6)){
                return 2;
            }
            else if(player2.contains(1) && player2.contains(4) && player2.contains(7)){
                return 2;
            }
            else if(player2.contains(2) && player2.contains(5) && player2.contains(8)){
                return 2;
            }
            else if(player2.contains(0) && player2.contains(4) && player2.contains(8)){
                return 2;
            }
            else if(player2.contains(2) && player2.contains(4) && player2.contains(6)){
                return 2;
            }
            return 3;
        }
//9 = ready

        /**
         * This method is an overridden method to get message from player 1 or 2 and sending back to the player.
         * @exception for player quitting
         */
        public void run() {
            String command;
            try {

                if (player_number == 1) {
                    player1_wri = new PrintWriter(player1_sock.getOutputStream(), true);
                    writers.add(player1_wri);

                    InputStreamReader streamReader1 = new InputStreamReader(player1_sock.getInputStream());
                    BufferedReader player1_reader = new BufferedReader(streamReader1);

                    while ((command = player1_reader.readLine()) != null) {

                        System.out.println("Command from player1: " + command);

                        String[] x = command.split(" ");


                        if (count <0){
                            player1_wri.println(command+ " " + player_number);

                        }
                        if (count> -1){

                            if (Integer.parseInt(x[0]) ==1 ){
                                player1.add(abs(Integer.parseInt(x[2])));
                                player1_wri.println(command + " " +  count);
                                player2_wri.println(command + " " +  count);
                            }}

                        count ++;

                        System.out.println("Server is broadcasting " + command);
                        if(count ==0){
                            player1_wri.println(9);
                            player2_wri.println(9);

                        }
                        if(result() == 1){
                            player2_wri.println(11);
                            player1_wri.println(11);
                            System.exit(0);

                        }
                        else if(result() == 2){
                            player2_wri.println(12);
                            player1_wri.println(12);
                            System.exit(0);

                        }
                        if(count ==9){
                            player2_wri.println(13);
                            player1_wri.println(13);
                            System.exit(0);

                        }
                    }

                }
                else {
                    player2_wri = new PrintWriter(player2_sock.getOutputStream(), true);
                    writers.add(player2_wri);

                    InputStreamReader streamReader2 = new InputStreamReader(player2_sock.getInputStream());
                    BufferedReader player2_reader = new BufferedReader(streamReader2);

                    while ((command = player2_reader.readLine()) != null) {

                        System.out.println("Command from player2: " + command);

                        String[] x = command.split(" ");

                        if (count <0){
                            player2_wri.println(command + " "+player_number);
                        }// 1 number

                        if(count >-1){

                            if (Integer.parseInt(x[0])== 1){
                                player2.add(abs(Integer.parseInt(x[2])));
                                player1_wri.println(command + " " + count);
                                player2_wri.println(command + " " +  count);
                            }
                        }
                        count ++;


                        System.out.println("Server is broadcasting " + command);

                        if(result() == 1){
                            player2_wri.println(11);
                            player1_wri.println(11);
                            System.exit(0);

                        }
                        else if(result() == 2){
                            player2_wri.println(12);
                            player1_wri.println(12);
                            System.exit(0);

                        }
                        if(count ==0){
                            player2_wri.println(9);
                            player1_wri.println(9);

                        }
                    }
                }


            }
            catch (Exception ex) {
                ex.printStackTrace();
                player2_wri.println(10);
                player1_wri.println(10);
                System.exit(0);
            }
        }


    }}