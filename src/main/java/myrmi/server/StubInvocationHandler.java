package myrmi.server;

import myrmi.exception.RemoteException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class StubInvocationHandler implements InvocationHandler, Serializable {
    private String host;
    private int port;
    private int objectKey;

    public StubInvocationHandler(String host, int port, int objectKey) {
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        System.out.printf("Stub created to %s:%d, object key = %d\n", host, port, objectKey);
    }

    public StubInvocationHandler(RemoteObjectRef ref) {
        this(ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws RemoteException, IOException,
            ClassNotFoundException, Throwable {
        /*TODO: implement stub proxy invocation handler here
         *  You need to do:
         * 1. connect to remote skeleton, send method and arguments
         * 2. get result back and return to caller transparently
         * */
//        throw new NotImplementedException();

        Object result = null;
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            // 1. Connect to the remote skeleton
            socket = new Socket();
            System.out.println(host + " " + port);
            socket.connect(new InetSocketAddress(host, port));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // 2. Send method name, parameter types, arguments, and object key
            out.writeInt(objectKey);
            out.writeObject(method.getName());
            out.writeObject(method.getParameterTypes());
            out.writeObject(args);
            out.flush();

            // 3. Get the result back
            int status = in.readInt(); // Read status of invocation
            switch (status) {
                case -1: // Invocation error
                    throw new RemoteException("Invocation error occurred at the skeleton side");
                case 0: // Exception thrown by method
                    Throwable exception = (Throwable) in.readObject();
                    throw exception;
                case 1: // Void method
                    break;
                case 2: // Non-void method
                    result = in.readObject();
                    break;
                default:
                    throw new RemoteException("Unknown response status from skeleton");
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RemoteException(e.getMessage());//"Error occurred during remote method invocation", e);
        } finally {
            // Close all resources
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                throw new RemoteException("Error occurred while closing resources", e);
            }
        }
        return result;

    }

}
