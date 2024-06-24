import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

public class Slave {
    public static void main(String[] args) {
        int port = 5001;
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

            // HashSet για την αποθήκευση των αριθμών
            HashSet<Integer> numbers = new HashSet<>();

            String inputText;
            while ((inputText = in.readLine()) != null) {
                int number = Integer.parseInt(inputText);
                // έλεγχος αν ο αριθμός έχει επαναληφθεί
                if (numbers.contains(number)) {
                    System.out.println("Number "+ number + " has repeated");
                    out.println("Number "+ number + " has repeated");
                } else {
                    // αν δεν έχει επαναληφθεί, προστίθεται στο HashSet
                    numbers.add(number);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
