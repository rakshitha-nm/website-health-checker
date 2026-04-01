package com.health.checker;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class WebsiteChecker {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // GUI PAGE
        server.createContext("/", exchange -> {

            String html =
                    "<html>" +
                    "<head>" +
                    "<title>Website Health Checker</title>" +
                    "</head>" +

                    "<body style='margin:0;height:100vh;display:flex;justify-content:center;align-items:center;background:#f2f2f2;font-family:Arial'>" +

                    "<div style='background:white;padding:40px;border-radius:10px;box-shadow:0px 0px 15px rgba(0,0,0,0.2);text-align:center'>" +

                    "<h1 style='color:#333'>Website Health Checker</h1>" +

                    "<p>Enter a website URL to check if it is UP or DOWN</p>" +

                    "<form action='/check'>" +

                    "<input type='text' name='url' placeholder='https://example.com' " +
                    "style='width:300px;height:35px;padding:5px;font-size:16px;border-radius:5px;border:1px solid gray'>" +

                    "<br><br>" +

                    "<button type='submit' " +
                    "style='background:#4CAF50;color:white;border:none;padding:10px 20px;font-size:16px;border-radius:5px;cursor:pointer'>" +

                    "Check Website</button>" +

                    "</form>" +

                    "</div>" +

                    "</body>" +
                    "</html>";

            exchange.sendResponseHeaders(200, html.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });

        // CHECK WEBSITE
        server.createContext("/check", exchange -> {

            String query = exchange.getRequestURI().getQuery();
            String website = query.split("=")[1];

            String result;

            try {

                URL url = new URL(website);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    result = "<h2 style='color:green'>Website is UP</h2>";
                } else {
                    result = "<h2 style='color:red'>Website might be DOWN</h2>";
                }

            } catch (Exception e) {

                result = "<h2 style='color:red'>Error checking website</h2>";
            }

            String response =
                    "<html>" +
                    "<body style='margin:0;height:100vh;display:flex;justify-content:center;align-items:center;background:#f2f2f2;font-family:Arial'>" +

                    "<div style='background:white;padding:40px;border-radius:10px;box-shadow:0px 0px 15px rgba(0,0,0,0.2);text-align:center'>" +

                    result +

                    "<br><br>" +

                    "<a href='/' style='text-decoration:none;font-size:18px;color:#4CAF50'>Check Another Website</a>" +

                    "</div>" +

                    "</body>" +
                    "</html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();

            os.write(response.getBytes());

            os.close();
        });

        server.start();

        System.out.println("Server started on port 8080");
    }
}