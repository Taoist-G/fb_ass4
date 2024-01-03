import myrmi.Remote;
import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import myrmi.registry.RegistryImpl;

public class JdkMainServer {

    public static void main(String[] args) {
        try {
            UserServicet userServer = new UserServiceImpl();
            Registry registry = LocateRegistry.createRegistry();
            registry.bind("rmi", userServer);
            System.out.println("server running......");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
