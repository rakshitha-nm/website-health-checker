package com.health.checker;

import java.net.HttpURLConnection;
import java.net.URL;

public class WebsiteChecker {

    public static void checkWebsite(String websiteUrl) {
        try {
            URL url = new URL(websiteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            long responseTime = connection.getConnectTimeout();

            if (responseCode == 200) {
                System.out.println("Website is UP");
            } else {
                System.out.println("Website is DOWN");
            }

            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Time: " + responseTime + "ms");

        } catch (Exception e) {
            System.out.println("Error checking website: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        String website = "https://www.google.com";

        checkWebsite(website);
    }
}