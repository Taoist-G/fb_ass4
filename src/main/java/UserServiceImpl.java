import myrmi.exception.RemoteException;
import myrmi.server.UnicastRemoteObject;

public class UserServiceImpl extends UnicastRemoteObject implements UserServicet {

    private static final long serialVersionUID = 1L;

    public UserServiceImpl() throws RemoteException {
    }

    public String sayHello(String username) {
        return "Welcome to JAVA RMI " + username;
    }
}
