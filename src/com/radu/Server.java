package com.radu;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private String ip;
    private Integer port;
    private ServerSocket serverSocket;
    private Map<String, Client> clientMap;

    public Server(String ip, Integer port) {
        try {
            this.ip = ip;
            this.port = port;
            this.serverSocket = new ServerSocket();
            clientMap = new ConcurrentHashMap<>(); // similar to HashTable but more powerful
            serverSocket.bind(new InetSocketAddress(this.ip, this.port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Client client = new Client(serverSocket.accept());
                if (clientMap.containsKey(client.getUsername())) {
                    client.refuse();
                } else {
                    clientMap.put(client.getUsername(), client);
                    client.accept();
                    System.out.println(String.format("\"%s\" connected.", client.getUsername()));
                    Thread t = new Thread(new ReceiverThread(client, this));
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // method called from multiple threads
    public void redirectMessage(Message message) {
        String receiver = message.getRecipient(); // thread safe
        if (clientMap.containsKey(receiver)) { // thread safe
            clientMap.get(receiver).sendMessage(message);
        } else {
            clientMap.get(message.getSender())
                    .sendMessage(new Message("Server",
                            message.getSender(),
                            String.format("User \"%s\" not connected.", receiver)));
        }
    }

    public void disconnectUser(String username) {
        clientMap.remove(username);
    }

}


