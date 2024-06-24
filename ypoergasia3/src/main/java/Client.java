import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        int port = 5000;
        // μήνυμα σε ποια πόρτα συνδέθηκε
        System.out.println("Connecting to " + port + " port");
        // δημιουργία socket
        try (Socket socket = new Socket("localhost", port)) {
            // δημιουργία PrintWriter για την αποστολή μηνυμάτων
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // δημιουργία BufferedReader για τη λήψη μηνυμάτων
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // μήνυμα σύνδεσης
            System.out.println("Connected!");

            // δημιουργία τυχαίου αριθμού
            new RandomNumber(out);

            // διάβασμα και εκτύπωση του μηνύματος
            String input;
            while ((input = in.readLine()) != null) {
                System.out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // κλάση για τη δημιουργία τυχαίων αριθμών
    private static class RandomNumber extends Thread {
        // τυχαίος αριθμός
        private final Random random = new Random();
        // μεταβλητή για αποστολή μηνυμάτων
        private final PrintWriter out;

        // constructor
        public RandomNumber(PrintWriter out) {
            this.out = out;
            start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000); // παύση για 1 δευτερόλεπτο
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.println(random.nextInt(100)); // τυχαίος αριθμός 1-100
            }
        }
    }
}
