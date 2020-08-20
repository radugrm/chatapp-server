package com.radu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Client {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String username;
    private Semaphore oosSemaphore;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            username = ois.readObject().toString();
            oosSemaphore = new Semaphore(1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) ois.readObject();
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {
        try {
            if (oos == null) { // the if is mandatory since the client has a single instance of ObjectInputStream
                System.out.println("new oos: " + username);
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
            oosSemaphore.acquire();
            oos.writeObject(message);
            oos.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            oosSemaphore.release();
        }
    }

    public void accept() {
        try {
            if (oos == null) { // the if is mandatory since the client has a single instance of ObjectInputStream
                System.out.println("new oos: " + username);
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
            oosSemaphore.acquire();
            oos.writeObject(true);
            oos.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            oosSemaphore.release();
        }
    }


    public void refuse() {
        try {
            if (oos == null) { // the if is mandatory since the client has a single instance of ObjectInputStream
                System.out.println("new oos: " + username);
                oos = new ObjectOutputStream(socket.getOutputStream());
            }
            oosSemaphore.acquire();
            oos.writeObject(false);
            oos.flush();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            oosSemaphore.release();
        }
    }
}
