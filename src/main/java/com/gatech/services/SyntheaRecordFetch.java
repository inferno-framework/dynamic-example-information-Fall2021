package com.gatech.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyntheaRecordFetch {
    // TODO: List out mustHave element
    public void fetchPatients(int noOfPatients) throws IOException {
        String base_url = "http://localhost:8000/?stu=3&p=" + noOfPatients;
        URL url = new URL(base_url);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "application/json");

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }
}
