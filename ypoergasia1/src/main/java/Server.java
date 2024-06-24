import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running and waiting for a client to connect...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                //μέθοδος για όταν συνδέεται ο client
                handleGame(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleGame(Socket clientSocket) {
        try (
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter serverOutput = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            serverOutput.println("Welcome to Rock-Scissors-Paper Game!");

            while (true) {
                // διάβασμα της επιλογής του client
                String clientChoice = clientInput.readLine().toLowerCase();
                System.out.println("User's choice: " + clientChoice);

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

            System.out.println("Connection closed.");
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
