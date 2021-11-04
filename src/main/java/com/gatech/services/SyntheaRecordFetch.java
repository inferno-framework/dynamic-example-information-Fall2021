package com.gatech.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyntheaRecordFetch {
    // TODO: List out mustHave element
    public void fetchPatients() throws IOException {
        URL url = new URL("http://localhost:8000/?stu=3&p=100");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("Accept", "application/json");

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }
}
