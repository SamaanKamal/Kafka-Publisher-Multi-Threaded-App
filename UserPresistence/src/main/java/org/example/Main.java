package org.example;

import com.sun.net.httpserver.*;
import org.example.Controller.UserController;
import org.example.Service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        UserController userController = new UserController(new UserService());
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/users", userController);
        server.setExecutor(null); // default executor
        server.start();

        System.out.println("🚀 HTTP server listening on port " + port);
    }
}