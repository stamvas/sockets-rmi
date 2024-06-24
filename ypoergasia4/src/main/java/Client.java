import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {

            Registry registry = LocateRegistry.getRegistry(1888);

            GameInterface gameServer = (GameInterface) registry.lookup("GameServer");

            Scanner userInput = new Scanner(System.in);
            System.out.println("Welcome to Rock-Scissors-Paper Game!");

            while (true) {

                // μήνυμα προς τον χρήστη
                System.out.print("Enter 'rock', 'scissors', 'paper', or 'exit' to end the game: ");

                // αποθήκευση της επιλογής του χρήστη
                String userChoice = userInput.nextLine().toLowerCase();

                // έλεγχος για σωστή επιλογή
                if (!isValidChoice(userChoice)) {
                    System.out.println("Invalid choice. Please enter 'rock', 'scissors', 'paper', or 'exit'.");
                    continue;
                }


                String result = gameServer.startGame(userChoice);

                // εκτύπωση του αποτελέσματος
                System.out.println(result);

                if (userChoice.equalsIgnoreCase("exit")) {
                    break;
                }

                // μήνυμα προς τον χρήστη
                System.out.print("Do you want to play again? (yes/no): ");
                String playAgain = userInput.nextLine().toLowerCase();

                if (!playAgain.equals("yes")) {
                    break;
                }
            }

            System.out.println("Connection closed.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // μέθοδος για την επιλογή του χρήστη αν είναι έγκυρη
    private static boolean isValidChoice(String choice) {
        return choice.equalsIgnoreCase("rock") || choice.equalsIgnoreCase("scissors") || choice.equalsIgnoreCase("paper") || choice.equalsIgnoreCase("exit");
    }
}
