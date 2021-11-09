package com.gatech.services.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Synthea {

    public Synthea() {}

    // Parse the profiles from Synthea

    public Map<String, JSONObject> findAttributeOnSynthea() {

        // Storing the resourceType as a key and the JSON object as it's respective value
        Map<String, JSONObject> profileByResource = new HashMap<>();
        JSONParser parser = new JSONParser();
        try {
            // Read the JSON file. This will be changed accordingly when we read the input file from the output folder in local repo that is mapped to container using Volume
            Object obj = readSyntheaFile();
            JSONObject jsonObject =  (JSONObject) obj;

            // Fetch the entire json array from entry
            JSONArray entries = (JSONArray) jsonObject.get("entry");

            // Loop through the entire entries and check for resource object. We are not concerned with FullUrl and other object
            for (int i = 0; i < entries.size(); i++) {
                final JSONObject eachJSONObject = (JSONObject) entries.get(i);
                for (Object key : eachJSONObject.keySet()) {
                    // If we found the resource, then we can now retrieve the value using resource Object and JSONObjects using resourceType objects
                    if (key.equals("resource")) {
                        JSONObject resourceObj = (JSONObject) eachJSONObject.get("resource");
                        String resourceType = (String) resourceObj.get("resourceType");
                        profileByResource.put(resourceType, resourceObj);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return profileByResource;
    }

    public Object readSyntheaFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/main/java/com/gatech/data/synthea/Aaron697_Brekke496_2fa15bc7-8866-461a-9000-f739e425860a.json"));
        return obj;
    }
}
