package com.gatech.services.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.io.File;

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
                JSONObject postResource = (JSONObject) eachJSONObject.get("request");
                String fullUrl = (String) eachJSONObject.get("fullUrl");

                for (Object key : eachJSONObject.keySet()) {
                    // If we found the resource, then we can now retrieve the value using resource Object and JSONObjects using resourceType objects
                    if (key.equals("resource")) {

                        JSONObject resourceObj = (JSONObject) eachJSONObject.get("resource");
                        String resourceType = (String) resourceObj.get("resourceType");

                        JSONObject finalResourceObj = new JSONObject();
                        finalResourceObj.put("resource", resourceObj);
                        finalResourceObj.put("request", postResource);
                        finalResourceObj.put("fullUrl", fullUrl);

                        profileByResource.put(resourceType, finalResourceObj);
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
        List<File> filesInFolder = Files.walk(Paths.get("/var/lib/docker/volumes/synthea/fhir"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        Object obj = parser.parse(new FileReader(filesInFolder.get(0)));
        return obj;
    }
}
