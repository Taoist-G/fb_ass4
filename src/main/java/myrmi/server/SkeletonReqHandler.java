package myrmi.server;

import myrmi.Remote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class SkeletonReqHandler extends Thread {
    private Socket socket;
    private Remote obj;
    private int objectKey;

    public SkeletonReqHandler(Socket socket, Remote remoteObj, int objectKey) {
        this.socket = socket;
        this.obj = remoteObj;
        this.objectKey = objectKey;
    }

    @Override
    public void run() {
//        int objectKey;
//        String methodName;
//        Class<?>[] argTypes;
//        Object[] args;
//        Object result;

        /*TODO: implement method here
         * You need to:
         * 1. handle requests from stub, receive invocation arguments, deserialization
         * 2. get result by calling the real object, and handle different cases (non-void method, void method, method throws exception, exception in invocation process)
         * Hint: you can use an int to represent the cases: -1 invocation error, 0 exception thrown, 1 void method, 2 non-void method
         *
         *  */
//        throw new NotImplementedException();

        try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            String methodName;
            Class<?>[] argTypes;
            Object[] args;
            Object result;

            // 1. Handle requests from stub, receive invocation arguments, deserialization
            int receivedObjectKey = ois.readInt();
            methodName = (String) ois.readObject();
            argTypes = (Class<?>[]) ois.readObject();
            args = (Object[]) ois.readObject();

            // 2. Get result by calling the real object, and handle different cases
            try {
                Method method = obj.getClass().getMethod(methodName, argTypes);
                result = method.invoke(obj, args);

                // Determine the method's return type to handle void and non-void methods accordingly
                if (method.getReturnType() == Void.TYPE) {
                    // Handle void method
                    oos.writeInt(1); // Indicating a void method
                    oos.writeObject(null);
                } else {
                    // Handle non-void method
                    oos.writeInt(2); // Indicating a non-void method
                    oos.writeObject(result);
                }
            } catch (InvocationTargetException ite) {
                // Handle method throws exception
                oos.writeInt(0); // Indicating a method exception
                oos.writeObject(ite.getCause());
            } catch (NoSuchMethodException | IllegalAccessException e) {
                // Handle invocation error
                oos.writeInt(-1); // Indicating an invocation error
                oos.writeObject(e);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
