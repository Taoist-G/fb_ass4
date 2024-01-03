package myrmi.server;

import myrmi.Remote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Skeleton extends Thread {
    static final int BACKLOG = 5;
    private Remote remoteObj;

    private String host;
    private int port;
    private int objectKey;

    public int getPort() {
        return port;
    }

    public Skeleton(Remote remoteObj, RemoteObjectRef ref) {
        this(remoteObj, ref.getHost(), ref.getPort(), ref.getObjectKey());
    }

    public Skeleton(Remote remoteObj, String host, int port, int objectKey) {
        super();
        this.remoteObj = remoteObj;
        this.host = host;
        this.port = port;
        this.objectKey = objectKey;
        this.setDaemon(false);
    }

    public Skeleton(Remote remoteObj, String host, int port) {
        this.remoteObj = remoteObj;
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        /*TODO: implement method here
         * You need to:
         * 1. create a server socket to listen for incoming connections
         * 2. use a handler thread to process each request (use SkeletonReqHandler)
         *  */
//        throw new NotImplementedException();
        try {
            ServerSocket serverSocket = new ServerSocket(this.port, BACKLOG);
            System.out.println("Skeleton is running on port " + this.port);

            // Continuously accept incoming connections
            while (!Thread.interrupted()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    SkeletonReqHandler skeletonReqHandler = new SkeletonReqHandler(clientSocket, remoteObj, objectKey);
                    skeletonReqHandler.start();
                } catch (SocketException e) {
                    System.out.println("Server Socket is closed, shutting down Skeleton");
                    break;
                } catch (IOException e) {
                    System.out.println("I/O error occurred when waiting for a connection");
                }
            }
        } catch (IOException e) {
            System.out.println("I/O error occurred when creating the server socket");
            e.printStackTrace();
        }

    }
}
