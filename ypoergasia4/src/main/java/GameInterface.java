import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
    String startGame(String userChoice) throws RemoteException;
}
