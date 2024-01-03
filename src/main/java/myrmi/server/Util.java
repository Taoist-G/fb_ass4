package myrmi.server;

import myrmi.Remote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Util {


    public static Remote createStub(RemoteObjectRef ref) {
        //TODO: finish here, instantiate an StubInvocationHandler for ref and then return a stub
//        throw new NotImplementedException();
        try {
            // Get the Class object for the interface using the interface name
            Class<?> interfaceClass = Class.forName(ref.getInterfaceName());

            // Create an InvocationHandler that will handle method calls on the stub
            InvocationHandler handler = new StubInvocationHandler(ref.getHost(), ref.getPort(), ref.getObjectKey());

            // Create and return the stub (proxy) for the remote object
            return (Remote) Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[] {interfaceClass},
                    handler
            );
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("The interface " + ref.getInterfaceName() + " could not be found.", e);
        }

    }
}
