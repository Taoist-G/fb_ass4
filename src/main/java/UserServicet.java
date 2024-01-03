import myrmi.Remote;
import myrmi.exception.RemoteException;

public interface UserServicet extends Remote {

    public String sayHello(String username) throws RemoteException;

}
