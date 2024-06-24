import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Server extends UnicastRemoteObject implements GameInterface {

    public Server() throws RemoteException {
        super();
    }

    @Override
    public String startGame(String userChoice) throws RemoteException {

        // δημιουργία τυχαίας επιλογής
        String serverChoice = getRandomChoice();

        // αποθήκευση του νικητή
        String result = determineWinner(userChoice, serverChoice);

        // επιστροφή του αποτελέσματος
        return "Computer chose: " + serverChoice + "\nResult: " + result;
    }

    // μέθοδος για τυχαία επιλογή
    private static String getRandomChoice() {
        String[] choices = {"rock", "scissors", "paper"};
        return choices[new Random().nextInt(choices.length)];
    }

    // μέθοδος για τον καθορισμό του νικητή
    private String determineWinner(String userChoice, String serverChoice) {
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

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1888);

            Server gameServer = new Server();

            registry.rebind("GameServer", gameServer);

            System.out.println("Game server is running...");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
