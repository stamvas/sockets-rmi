import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 12345); // δημιουργία socket
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter clientOutput = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println(serverInput.readLine());

            while (true) {
                String userChoice;
                // loop για το αν ο παίκτης έκανε σωστή επιλογή
                do {
                    System.out.print("Enter 'rock', 'scissors', 'paper', or 'exit' to end the game: ");
                    userChoice = userInput.readLine().toLowerCase();
                    if (!isValidChoice(userChoice)) {
                        System.out.println("Invalid choice. Please enter 'rock', 'scissors', 'paper' or 'exit'.");
                    }
                } while (!isValidChoice(userChoice));

                clientOutput.println(userChoice);

                if (userChoice.equalsIgnoreCase("exit")) {
                    break;  // αν επιλέξει exit βγαίνει απο το loop
                }

                // μηνύματα για τις επιλογές
                System.out.println(serverInput.readLine());
                System.out.println(serverInput.readLine());

                // loop για το αν ο παίκτης θέλει να ξανά παίξει
                String playAgain;
                do {
                    System.out.print("Do you want to play again? (yes/no): ");
                    playAgain = userInput.readLine().toLowerCase();
                    if (!playAgain.equals("yes") && !playAgain.equals("no")) {
                        System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
                    }
                } while (!playAgain.equals("yes") && !playAgain.equals("no"));


                // αν επιλέξει 'no' βγαίνει απο το loop
                if (playAgain.equals("no")) {
                    clientOutput.println("exit");
                    break;
                }
            }

            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // μέθοδος για την επιλογή του χρήστη αν είναι έγκυρη
    private static boolean isValidChoice(String choice) {
        return choice.equalsIgnoreCase("rock") || choice.equalsIgnoreCase("scissors") || choice.equalsIgnoreCase("paper") || choice.equalsIgnoreCase("exit");
    }
}
