package com.health.checker;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.sun.net.httpserver.HttpServer;

public class WebsiteChecker {

    public static String checkWebsite(String websiteUrl) {
        try {
            URL url = new URL(websiteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode < 400) {
                return "Website is UP (Response Code: " + responseCode + ")";
            } else {
                return "Website is DOWN (Response Code: " + responseCode + ")";
            }

        } catch (Exception e) {
            return "Error checking website: " + e.getMessage();
        }
    }

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {

            String html = "<html><head><title>Website Health Checker</title></head>"
                    + "<body style='font-family:Arial;text-align:center;margin-top:120px;'>"
                    + "<h1> Website Health Checker</h1>"
                    + "<form method='get'>"
                    + "<input type='text' name='url' placeholder='Enter website URL' style='padding:10px;width:300px;'>"
                    + "<br><br>"
                    + "<button style='padding:10px 20px;'>Check Website</button>"
                    + "</form>";

            String query = exchange.getRequestURI().getQuery();

            if (query != null && query.startsWith("url=")) {

                String url = query.substring(4);

                if (!url.startsWith("http")) {
                    url = "https://" + url;
                }

                String result = checkWebsite(url);

                html += "<h2 style='margin-top:30px;color:blue'>" + result + "</h2>";
            }

            html += "</body></html>";

            exchange.sendResponseHeaders(200, html.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });

        server.start();

        System.out.println("Server started on port 8080");
    }
}