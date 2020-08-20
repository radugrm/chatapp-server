package com.radu;

import java.io.IOException;
import java.net.SocketException;

public class ReceiverThread implements Runnable {
    private Client client;
    private Server server;

    public ReceiverThread(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = client.readMessage();
                server.redirectMessage(message);
            }
        } catch (SocketException e) {
            server.disconnectUser(client.getUsername());
            client.disconnect();
            System.out.println(String.format("\"%s\" disconnected.", client.getUsername()));
            return;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
