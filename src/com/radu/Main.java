package com.radu;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Server server = new Server("localhost", 9999);
        server.start();
    }
}
