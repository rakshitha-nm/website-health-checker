package com.health.checker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
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
                return "UP (Response Code: " + responseCode + ")";
            } else {
                return "DOWN (Response Code: " + responseCode + ")";
            }

        } catch (Exception e) {
            return "Error checking website: " + e.getMessage();
        }
    }

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        // Serve background image
        server.createContext("/background.jpg", exchange -> {

            InputStream is = WebsiteChecker.class.getClassLoader()
                    .getResourceAsStream("background.jpg");

            if (is == null) {
                exchange.sendResponseHeaders(404, 0);
                return;
            }

            byte[] imageBytes = is.readAllBytes();

            exchange.getResponseHeaders().set("Content-Type", "image/jpeg");
            exchange.sendResponseHeaders(200, imageBytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(imageBytes);
            os.close();
        });

        server.createContext("/", exchange -> {

            String html = "<html><head><title>Website Health Checker</title>"
                    + "<style>"
                    + "body{"
                    + "font-family:Arial;"
                    + "height:100vh;"
                    + "margin:0;"
                    + "display:flex;"
                    + "justify-content:center;"
                    + "align-items:center;"
                    + "background-image:url('/background.jpg');"
                    + "background-size:cover;"
                    + "background-position:center;"
                    + "}"
                    + ".container{"
                    + "background:white;"
                    + "padding:40px;"
                    + "border-radius:10px;"
                    + "box-shadow:0px 0px 15px rgba(0,0,0,0.3);"
                    + "text-align:center;"
                    + "width:400px;"
                    + "}"
                    + "input{padding:10px;width:90%;margin-top:10px;font-size:16px;}"
                    + "button{padding:10px 20px;margin-top:15px;background:#007bff;color:white;border:none;border-radius:5px;cursor:pointer;}"
                    + "button:hover{background:#0056b3;}"
                    + "a{display:block;margin-top:10px;color:blue;font-size:16px;}"
                    + "</style>"
                    + "</head><body>"
                    + "<div class='container'>"
                    + "<h1>Website Health Checker</h1>"
                    + "<form method='get'>"
                    + "<input type='text' name='url' placeholder='Enter website URL'>"
                    + "<br>"
                    + "<button>Check Website</button>"
                    + "</form>";

            String query = exchange.getRequestURI().getQuery();

            if (query != null && query.startsWith("url=")) {

                String url = query.substring(4);

                if (!url.startsWith("http")) {
                    url = "https://" + url;
                }

                String result = checkWebsite(url);

                if (result.contains("UP")) {

                    html += "<h2 style='margin-top:25px;color:green'>Website is " + result + "</h2>";
                    html += "<a href='" + url + "' target='_blank'>Click here to open the website</a>";

                } else {

                    html += "<h2 style='margin-top:25px;color:red'>Website is " + result + "</h2>";
                }
            }

            html += "</div></body></html>";

            exchange.sendResponseHeaders(200, html.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });

        server.start();

        System.out.println("Server started on port 8080");
    }
}