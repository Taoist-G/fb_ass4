package myrmi.server;

import myrmi.Remote;
import myrmi.exception.RemoteException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class UnicastRemoteObject implements Remote, java.io.Serializable {
    int port;

    protected UnicastRemoteObject() throws RemoteException {
        this(0);
    }

    protected UnicastRemoteObject(int port) throws RemoteException {
        this.port = port;
        exportObject(this, port);
    }

    public static Remote exportObject(Remote obj) throws RemoteException {
        return exportObject(obj, 0);
    }

    public static Remote exportObject(Remote obj, int port) throws RemoteException {
        return exportObject(obj, "127.0.0.1", port);
    }

    /**
     * 1. create a skeleton of the given object ``obj'' and bind with the address ``host:port''
     * 2. return a stub of the object ( Util.createStub() )
     **/
    public static Remote exportObject(Remote obj, String host, int port) throws RemoteException {
        //TODO: finish here
//        throw new NotImplementedException();
        // Check if the object is already exported
        if (obj instanceof UnicastRemoteObject && ((UnicastRemoteObject) obj).port != 0) {
            throw new RemoteException("Object already exported");
        }
        Skeleton skeleton = new Skeleton(obj, host, port, 0);
        skeleton.start();

        // Create a RemoteObjectRef instance for the object
        String interfaceName = obj.getClass().getInterfaces()[0].getName();
        RemoteObjectRef ref = new RemoteObjectRef(host, port, 0, interfaceName);

        // Create and return the stub for the object
        Remote stub = Util.createStub(ref);
        return stub;

    }
}
