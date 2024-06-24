import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Master {
    private static final int CLIENT_PORT = 5000; // πόρτα για τον CLIENT
    private static final int SLAVE_PORT = 5001; // πόρτα για τον SLAVE

    // δημιουργία νημάτων
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    // λίστα για client
    private final List<ClientThread> clientThreads = new ArrayList<>();
    // λίστα για slave
    private final List<SlaveThread> slaveThreads = new ArrayList<>();
    // τυχαίος αριθμός
    private Random random = new Random();

    public static void main(String[] args) throws IOException {
        new Master(); // δημιουργία νέου αντικειμένου
    }

    // constructor
    public Master() throws IOException {
        // μέθοδος startServer με παράμετρο 5000
        startServer(CLIENT_PORT, true);

        // μέθοδος startServer με παράμετρο 5001
        startServer(SLAVE_PORT, false);
    }

    // μέθοδος για την εκκίνηση του master
    private void startServer(int port, boolean isClientServer) throws IOException {
        executorService.execute(() -> { // // Δημιουργία νέου νήματος
            // δημιουργία serverSocket
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server is listening on port " + port);

                // αναμονή για νέα σύνδεση
                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();

                    // εκκίνηση του νήματος για Client ή Slave
                    if (isClientServer) {
                        new ClientThread(socket);
                    } else {
                        new SlaveThread(socket);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // μέθοδος επεξεργασίας μηνύματος
    private synchronized void processMessageWithSlaves(String message) {
        if (!slaveThreads.isEmpty()) {
            int randomIndex = random.nextInt(slaveThreads.size());
            SlaveThread slaveThread = slaveThreads.get(randomIndex);
            slaveThread.processMessage(message);
        }
    }

    // μέθοδος για αποστολή μηνυμάτων
    private synchronized void sendMsgToClients(String message) {
        for (ClientThread clientThread : clientThreads) {
            clientThread.sendMessage(message);
        }
    }

    // μέθοδος για προσθήκη client
    private synchronized void newClient(ClientThread clientThread) {
        clientThreads.add(clientThread);
    }


    // μέθοδος για προσθήκη slave
    private synchronized void newSlave(SlaveThread slaveThread) {
        slaveThreads.add(slaveThread);
    }


    // κλάση νήμα για τον Client
    private class ClientThread implements Runnable {
        private final Socket socket;
        private PrintWriter out;

        // constructor
        private ClientThread(Socket socket) {
            this.socket = socket;
            executorService.execute(this);
        }

        @Override
        public void run() { // εκτύπωση οτι συνδέθηκε Client
            System.out.println("Client connected");
            newClient(this);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                this.out = out;

                // επεξεργασία μηνυμάτων
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    processMessageWithSlaves(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // μέθοδος για αποστολή μηνυμάτων
        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }

    // κλάση νήμα για τον Slave
    private class SlaveThread implements Runnable {
        private final Socket socket;
        private PrintWriter out;

        // constructor
        private SlaveThread(Socket socket) {
            this.socket = socket;
            executorService.execute(this);
        }

        @Override
        public void run() { // εκτύπωση οτι συνδέθηκε Slave
            System.out.println("Slave connected");
            newSlave(this);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                this.out = out;

                // αποστολή μηνυμάτων
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sendMsgToClients(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // μέθοδος για επεξεργασία μηνύματος
        public synchronized void processMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }
}
