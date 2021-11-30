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

    public void generate(String ig) throws IOException, ParseException {

        ImplementationGuide implementationGuide = new ImplementationGuide();

        Synthea synthea = new Synthea();
        Map<String, JSONObject> resourceAndJSON = synthea.findAttributeOnSynthea();
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        File[] files = new File("src/main/java/com/gatech/data/implementationGuide/"+ig).listFiles(File::isFile);

        JSONObject example = new JSONObject();

        JSONParser parser = new JSONParser();
        for(File profile : files != null ? files : new File[0]){
            JSONObject data = implementationGuide.readImplementationGuide(profile.getAbsolutePath());
            String resourceType = implementationGuide.findResourceType(data);
            if(resourceAndJSON.get(resourceType) == null){
                String fullUrl = String.format("urn:uuid:%s", UUID.randomUUID());
                JSONObject request = (JSONObject) parser.parse(String.format("{\"method\": \"POST\",\"url\": \"%s\"}", resourceType));
                JSONObject generatedData = generateDataForProfile(data);
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

    public JSONObject generateDataForProfile(JSONObject data) throws IOException {
        ImplementationGuide implementationGuide = new ImplementationGuide();
        JSONObject snapshot = (JSONObject) data.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");
        List<String> allAttributesOnImpGuide = implementationGuide.findAllElements(element);
        ValueGenerator generator=new ValueGenerator();

        JSONObject generatedData = new JSONObject();

        Boolean trigger=false;
        List<String> attributes=new ArrayList<String>();

        Map<String, JSONObject> attributeElement = new HashMap<>();

        for (String attribute:allAttributesOnImpGuide) {
            for (Object slide : element) {
                JSONObject jsonObject2 = (JSONObject) slide;
                String id = (String) jsonObject2.get("id");

                if (id.contains(attribute)) {
                    attributeElement.put(attribute, jsonObject2);
                    break;
                }
            }
        }

        for(int i = 0; i<allAttributesOnImpGuide.size()-1; i++){
            String attr=allAttributesOnImpGuide.get(i);
            String next=allAttributesOnImpGuide.get(i+1);
            if (trigger){
                String[] attrs = attr.split("\\.");
                String[] nexts = next.split("\\.");
                attributes.add(attr);
                if (!attrs[0].equals(nexts[0])){
                    trigger=false;
                    data = generator.generateComplex(attributes, attributeElement);
                    generatedData.put(attr, data.get(attr));
                    attributes=new ArrayList<String>();
                    if (i==allAttributesOnImpGuide.size()-1){
                        data = generator.generate(next, attributeElement.get(next));
                        generatedData.put(attr, data.get(attr));
                    }
                }else if (i==allAttributesOnImpGuide.size()-1){
                    attributes.add(next);
                    data = generator.generateComplex(attributes, attributeElement);
                    generatedData.put(attr, data.get(attr));
                }
            }else{
                if (next.contains(attr) ){
                    trigger=true;
                }else{
                    data = generator.generate(attr, attributeElement.get(attr));
                    generatedData.put(attr, data.get(attr));
                }
            }
        }
        return generatedData;
    }
}
