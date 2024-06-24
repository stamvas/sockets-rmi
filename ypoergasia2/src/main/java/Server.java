import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    // μεταβλητή για την καταμέτρηση των client
    private static final AtomicInteger clientCounter = new AtomicInteger(1);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running and waiting for a client to connect...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientName = "Client " + clientCounter.getAndIncrement();
                System.out.println(clientName + " connected.");

                // δημιουργία thread για κάθε client
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientName);
                clientHandler.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {

        private final Socket clientSocket;
        private final String clientName;

        public ClientHandler(Socket clientSocket, String clientName) {
            this.clientSocket = clientSocket;
            this.clientName = clientName;
        }

        @Override
        public void run() {
            try (
                    BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter serverOutput = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                serverOutput.println("Welcome to Rock-Scissors-Paper Game!");

                while (true) {
                    // διάβασμα της επιλογής του client
                    String clientChoice = clientInput.readLine().toLowerCase();
                    System.out.println(clientName + " choice: " + clientChoice);

                    if (clientChoice.equalsIgnoreCase("exit")) {
                        break;  // αν επιλέξει exit βγαίνει απο το loop
                    }

                    // τυχαία επιλογή
                    String serverChoice = getRandomChoice();
                    System.out.println("Server's choice: " + serverChoice);

                    // τρέχει η determineWinner για τον καθορισμό του νικητή
                    // και αποθηκεύεται στη result
                    String result = determineWinner(clientChoice, serverChoice);

                    // μηνύματα για τις επιλογές
                    serverOutput.println("Server chose: " + serverChoice);
                    serverOutput.println("Result: " + result);
                }

                System.out.println(clientName + " closes the connection.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // μέθοδος για τη 'τυχαία' επιλογή
        private static String getRandomChoice() {
            String[] choices = {"rock", "scissors", "paper"};
            return choices[new Random().nextInt(choices.length)];
        }

        // μέθοδος για τον καθορισμό του νικητή
        private static String determineWinner(String userChoice, String serverChoice) {
            if (userChoice.equals(serverChoice)) {
                return "It's a tie!";
            } else if (
                    (userChoice.equals("rock") && serverChoice.equals("scissors")) ||
                            (userChoice.equals("scissors") && serverChoice.equals("paper")) ||
                            (userChoice.equals("paper") && serverChoice.equals("rock"))
            ) {
                return "You win!";
            } else {
                return "You lose!";
            }
        }
    }
}
