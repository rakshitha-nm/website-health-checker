package com.health.checker;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class WebsiteChecker {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/check", (HttpExchange exchange) -> {

            String query = exchange.getRequestURI().getQuery();
            String url = query.split("=")[1];

            String result = checkWebsite(url);

            exchange.sendResponseHeaders(200, result.length());
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();

        });

        server.start();
        System.out.println("Server started on port 8080");
    }

    public static String checkWebsite(String websiteUrl) {

        try {

            URL url = new URL(websiteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            long start = System.currentTimeMillis();

            int responseCode = connection.getResponseCode();

            long end = System.currentTimeMillis();
            long responseTime = end - start;

            if (responseCode == 200) {
                return "Website is UP\nResponse Code: " + responseCode +
                        "\nResponse Time: " + responseTime + " ms";
            } else {
                return "Website is DOWN\nResponse Code: " + responseCode;
            }

        } catch (Exception e) {
            return "Error checking website: " + e.getMessage();
        }

    }
}