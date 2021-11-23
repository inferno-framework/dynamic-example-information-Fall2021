package com.gatech.services;

import com.gatech.services.parser.ImplementationGuide;
import com.gatech.services.parser.Synthea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExampleGenerator {

    public void generate() throws IOException, ParseException {

        ImplementationGuide implementationGuide = new ImplementationGuide();

        Synthea synthea = new Synthea();
        Map<String, JSONObject> resourceAndJSON = synthea.findAttributeOnSynthea();
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        File[] files = new File("src/main/java/com/gatech/data/implementationGuide/us-core/").listFiles(File::isFile);

        JSONObject example = new JSONObject();

        String fullUrl = "urn:uuid:4d2b6ddd-47a7-4124-96fc-ca2ae8effc11";
        JSONParser parser = new JSONParser();
        JSONObject request = (JSONObject) parser.parse("{\"method\": \"POST\",\"url\": \"Generated\"}");
        for(File profile : files != null ? files : new File[0]){
            JSONObject data = implementationGuide.readImplementationGuide(profile.getAbsolutePath());
            String resourceType = implementationGuide.findResourceType(data);
            if(resourceAndJSON.get(resourceType) == null){
                JSONObject generatedData = generateDataForProfile(profile);
                JSONObject finalData = new JSONObject();
                finalData.put("fullUrl", fullUrl);
                finalData.put("resource", generatedData);
                finalData.put("request", request);
                items.add(finalData);
            }
            else {
                items.add(resourceAndJSON.get(resourceType));
            }

        }
        example.put("entry", items);
        example.put("type", "transaction");
        example.put("resourceType", "Bundle");
        System.out.println(example);
    }

    public JSONObject generateDataForProfile(File profileName) throws IOException {
        ImplementationGuide implementationGuide = new ImplementationGuide();
        JSONObject data = implementationGuide.readImplementationGuide(profileName.getAbsolutePath());
        JSONObject snapshot = (JSONObject) data.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");
        List<String> allAttributesOnImpGuide = implementationGuide.findAllElements(element);
        ValueGenerator generator=new ValueGenerator();

        JSONObject generatedData = new JSONObject();
        for(String attr : allAttributesOnImpGuide){
            data = generator.generate(attr, profileName.getName());
            generatedData.put(attr, data.get(attr));
        }
        return generatedData;
    }
}
