package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    private final Socket netSocket;
    private final InputStream in;
    private final OutputStream out;

    private int clientID;

    public ClientHandler(Socket skt) throws IOException {
        this.netSocket = skt;

        this.in = netSocket.getInputStream();
        this.out = netSocket.getOutputStream();
    }

    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.netSocket.close();
    }

    @Override
    public void run() {
        while (netSocket.isConnected()) {
            try {
                byte[] arr = new byte[2048];
                int read = in.read(arr);
                if (read > 0) {
                    String[] input = new String(arr, StandardCharsets.US_ASCII).split(":");

                    // if the message is tagged as "INTRODUCTION", it identifies a reply from the ESP32, which contains the client ID
                    if (input[0].equals("INTRODUCTION")) {
                        clientID = Integer.parseInt(input[1]);
                        Tools.log("The client connected is with id " + clientID + "!");
                        formRequest("TEST_REQUEST");
                    }
                    String trim = input[0].trim();
                    if (trim.equals("testResponse")) {
                        Tools.log("The client logged: " + "'"+ trim + "'");
                        formRequest("TEST_REQUEST");
                    }
                }

            } catch (IOException e) {
                Tools.log(this.getClass().getName(), "Exception at client ID" + clientID + "!");
                e.printStackTrace();
            }
        }
        Tools.log(this.getClass().getName(), "Client ID '" + clientID + "' disconnected!");
        Tools.clients.remove(this);
    }

    public void formRequest(String request) throws IOException {
        byte[] bytes = request.getBytes(StandardCharsets.US_ASCII);
        Tools.log("TX: " + request);
        out.write(bytes);
        out.flush();
    }

}