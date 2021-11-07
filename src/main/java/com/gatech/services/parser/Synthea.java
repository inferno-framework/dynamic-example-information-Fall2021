package com.gatech.services.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Synthea {

    /**
     * This class will contain all the methods to parse column from Synthea json in each area
     * Parser methods includes for:
     * Identifier, code, SerialNumber, Insurance, Period, LifeCycleStatus, Component, CodeableConcept, PrimaryCourse, Result
     * Regardless of which resource the attribute belongs to, we will store each of these attributes in a list, and will later merge to get the final list of attributes
     */

    public Synthea() {}

    // Parse the entries from synthea to be used for all the methods in SyntheaDataParser

    public List<String> findAttributeOnSynthea() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("src/main/java/com/gatech/data/synthea/Aaron697_Brekke496_2fa15bc7-8866-461a-9000-f739e425860a.json"));
            JSONObject jsonObject =  (JSONObject) obj;
            List<String> resourceList = new ArrayList<>();
            // Start with retrieving the entry
            JSONArray entries = (JSONArray) jsonObject.get("entry");
            for (Object entry : entries) {
                // Each entry will contain blob of resource information
                JSONObject updateObject = new JSONObject((Map) entry);
                System.out.println("---------------");
                System.out.println(updateObject.get("resource"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
