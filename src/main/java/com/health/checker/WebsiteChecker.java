package com.health.checker;

import java.net.HttpURLConnection;
import java.net.URL;

public class WebsiteChecker {

    public static void checkWebsite(String websiteUrl) {
        try {

            System.out.println("Checking website: " + websiteUrl);

            long startTime = System.currentTimeMillis();

            URL url = new URL(websiteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;

            if (responseCode == 200) {
                System.out.println("Website is UP");
            } else {
                System.out.println("Website is DOWN");
            }

            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Time: " + responseTime + " ms");

        } catch (Exception e) {
            System.out.println("Error checking website: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        String website = "http://localhost:8080/check?url=https://github.com";

        checkWebsite(website);
    }
}