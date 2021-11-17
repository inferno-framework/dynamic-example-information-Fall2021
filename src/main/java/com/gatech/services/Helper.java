package com.gatech.services;

import com.gatech.services.parser.ImplementationGuide;
import com.gatech.services.parser.Synthea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

    public Helper() {}

    public Map<String, List<String>> findMissingAttributeByProfile() {
        // Get all attribute from implementation
        ImplementationGuide implementationGuide = new ImplementationGuide();
        JSONObject data = implementationGuide.readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/us-core-patient.json");
        JSONObject snapshot = (JSONObject) data.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");

        List<String> allAttributesOnImpGuide = implementationGuide.findAllElements(element);

        String profileName = implementationGuide.findResourceType(data);

        // Get resource and it's json object from Synthea
        Synthea synthea = new Synthea();
        Map<String, JSONObject> resourceAndJSON = synthea.findAttributeOnSynthea();

        return compareImpGuideAndSynthea(profileName, allAttributesOnImpGuide, resourceAndJSON);

    }

    public static Map<String, List<String>> compareImpGuideAndSynthea(String profileName, List<String> allAttributesOnImpGuide, Map<String, JSONObject> resourceAndJSON) {

        Map<String, List<String>> missingAttributeOnSynthea = new HashMap<>();
        List<String> missingAttributes = new ArrayList<>();
        for (Map.Entry<String, JSONObject> entry : resourceAndJSON.entrySet()) {
            String key = entry.getKey();
            if (key.equals(profileName)) {
                JSONObject syntheaKeyJSONObj = entry.getValue();
                for (String impAttr : allAttributesOnImpGuide) {
                    if (!syntheaKeyJSONObj.containsKey(impAttr)) {
                        missingAttributes.add(impAttr);
                    }
                }
                missingAttributeOnSynthea.put(profileName, missingAttributes);
            }
        }
        return missingAttributeOnSynthea;
    }

    // Retrieve all the profile for US-Core implementation guide
    public List<String> getAllImplementationGuideProfile() {
        File folder = new File("src/main/java/com/gatech/data/implementationGuide");
        File[] listOfFiles = folder.listFiles();

        List<String> profileList = new ArrayList<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                String[] fileNameSplit = fileName.split("\\.");
                String profileNameWithUSCorePre = fileNameSplit[0];
                String[] splitProfileNameWithUSCorePre = profileNameWithUSCorePre.split("us-core-");
                String profileName = splitProfileNameWithUSCorePre[splitProfileNameWithUSCorePre.length - 1];
                profileList.add(profileName);
            }
        }
        return profileList;
    }
}