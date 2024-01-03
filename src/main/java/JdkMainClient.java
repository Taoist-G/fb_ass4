import myrmi.registry.LocateRegistry;
import myrmi.registry.Registry;
import myrmi.registry.RegistryImpl;

public class JdkMainClient {
    public static void main(String[] args) {

        try {
            // Gets the proxy instance of the remote registry
            Registry registry = LocateRegistry.getRegistry();
            // Use a proxy instance to find a remote object
            UserServicet userService = (UserServicet) registry.lookup("rmi");

            System.out.println(userService.sayHello("taoist"));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
