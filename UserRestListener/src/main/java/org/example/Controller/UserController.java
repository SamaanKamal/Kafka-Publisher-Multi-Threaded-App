package org.example.Controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.Entity.User;
import org.example.Service.UserService;
import org.example.Util.JSONUtil;

import java.io.*;
import java.util.stream.Collectors;

public class UserController implements HttpHandler {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        InputStream body = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(body))
                .lines().collect(Collectors.joining("\n"));

        try {
            // Deserialize user object from request
            User user = JSONUtil.fromJson(requestBody, User.class);

            // Delegate user creation and notification handling to UserService
            // userService.handleUserCreation(user);
            userService.sendUserToKafka(user);

            String response = "✅ User and notification saved and published.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            System.out.println("🔔 User and notification handled.");
        } catch (Exception e) {
            e.printStackTrace();
            String response = "❌ Error processing user.";
            exchange.sendResponseHeaders(500, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
