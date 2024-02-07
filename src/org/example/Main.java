package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        try {
            // startup args
            int port = args.length > 0 ? Integer.parseInt(args[0]) : 32410;
            int maxClients = args.length > 1 ? Integer.parseInt(args[2]) : 10;

            // startup parameters info
            Tools.log("Main", "Server started with parameters: ");
            if (args.length > 0) Tools.log("Main", args);
            else Tools.log("Main", "Default parameters");

            // server socket and the threadpool, where the client threads get executed
            ServerSocket server = new ServerSocket(port);
            ExecutorService pool = Executors.newFixedThreadPool(maxClients);

            // main loop
            while (true) {
                if (Tools.clients.size() < maxClients) {

                    // connection establishment
                    Socket clientSocket = server.accept();
                    ClientHandler client = new ClientHandler(clientSocket);

                    Tools.log("Main", "New client connected from " + clientSocket.getRemoteSocketAddress());

                    // starting the client operation
                    client.formRequest("REQ_ID");
                    pool.execute(client);
                    Tools.clients.add(client);
                }
            }
        } catch (IOException ioe) {
            Tools.log("Main", "IOException at MAIN");
            ioe.printStackTrace();
        }
    }
}
