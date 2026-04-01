package com.health.checker;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class WebsiteChecker {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // GUI page
        server.createContext("/", exchange -> {

            String html =
                    "<html>" +
                    "<head><title>Website Health Checker</title></head>" +
                    "<body style='font-family:Arial;text-align:center;margin-top:50px'>" +
                    "<h2>Website Health Checker</h2>" +
                    "<form action='/check'>" +
                    "<input type='text' name='url' placeholder='Enter Website URL' style='width:300px;height:30px'>" +
                    "<br><br>" +
                    "<button type='submit'>Check Website</button>" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            exchange.sendResponseHeaders(200, html.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });

        // Check website API
        server.createContext("/check", exchange -> {

            String query = exchange.getRequestURI().getQuery();
            String url = query.split("=")[1];

            String result;

            try {

                URL website = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) website.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                int code = connection.getResponseCode();

                if (code == 200) {
                    result = "<h2>Website is UP</h2>";
                } else {
                    result = "<h2>Website might be DOWN</h2>";
                }

            } catch (Exception e) {
                result = "<h2>Error checking website</h2>";
            }

            String response =
                    "<html><body style='font-family:Arial;text-align:center'>" +
                    result +
                    "<br><a href='/'>Check Another</a>" +
                    "</body></html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();

        System.out.println("Server started on port 8080");
    }
}