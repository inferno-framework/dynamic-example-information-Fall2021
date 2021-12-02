package com.gatech.services;

import com.gatech.services.parser.ImplementationGuide;
import com.gatech.services.parser.Synthea;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ExampleGenerator {

    public JSONObject generate(String ig) throws IOException, ParseException {

        ImplementationGuide implementationGuide = new ImplementationGuide();

        Synthea synthea = new Synthea();
        Multimap<String, JSONObject> resourceAndJSON = synthea.findAttributeOnSynthea();
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        List<String> keysFetchedFromSynthea=new ArrayList<String>();
        File[] files = new File("src/main/java/com/gatech/data/implementationGuide/"+ig).listFiles(File::isFile);

        JSONObject example = new JSONObject();

        JSONParser parser = new JSONParser();
        JSONObject generatedData = new JSONObject();

        Map<String, JSONObject> attributeElement = new HashMap<>();

        for(File profile : files != null ? files : new File[0]){
            JSONObject data = implementationGuide.readImplementationGuide(profile.getAbsolutePath());
            String resourceType = implementationGuide.findResourceType(data);
            JSONObject snapshot = (JSONObject) data.get("snapshot");
            JSONArray element = (JSONArray) snapshot.get("element");
            List<String> allAttributesOnImpGuide = implementationGuide.findAllElements(element);
            if(resourceAndJSON.get(resourceType).isEmpty()){
                String fullUrl = String.format("urn:uuid:%s", UUID.randomUUID());
                JSONObject request = (JSONObject) parser.parse(String.format("{\"method\": \"POST\",\"url\": \"%s\"}", resourceType));
                generatedData = generateDataForProfile(data);
                JSONObject finalData = new JSONObject();
                finalData.put("fullUrl", fullUrl);
                finalData.put("resource", generatedData);
                finalData.put("request", request);
                items.add(finalData);
            }
            else if (keysFetchedFromSynthea.contains(resourceType));
            else{
                Map<String, List<String>> missingAttributeByProfile = Helper.findMissingAttributeByProfile(profile.getAbsolutePath());
                List<String> missingAttributesOnSynthea = new ArrayList<String>(missingAttributeByProfile.get(resourceType));
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

                generatedData = generateDataForListOfAttributes(missingAttributesOnSynthea, attributeElement);
                Collection<JSONObject> syntheaData = resourceAndJSON.get(resourceType);
                for (JSONObject eachSyntheaData : syntheaData){
                    JSONObject finalData = new JSONObject();
                    JSONObject finalResource = new JSONObject();
                    HashMap result = new Gson().fromJson(eachSyntheaData.get("resource").toString(), HashMap.class);

                    finalResource.putAll(result);
                    finalResource.putAll(generatedData);

                    Object fullUrl = eachSyntheaData.get("fullurl");
                    if(fullUrl != null) {
                        finalData.put("fullUrl", fullUrl);
                    }
                    finalData.put("resource", finalResource);
                    finalData.put("request", eachSyntheaData.get("request"));
                    items.add(finalData);
                }
                items.addAll(resourceAndJSON.get(resourceType));
                keysFetchedFromSynthea.add(resourceType);
            }

        }
        example.put("entry", items);
        example.put("type", "transaction");
        example.put("resourceType", "Bundle");
        return example;
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

        return generateDataForListOfAttributes(allAttributesOnImpGuide, attributeElement);
    }

    public JSONObject generateDataForListOfAttributes(List<String> attributesToBeGenerated, Map<String, JSONObject> attributeElement) throws IOException {
        JSONObject data = new JSONObject();
        JSONObject generatedData = new JSONObject();
        Boolean trigger=false;
        List<String> attributes=new ArrayList<String>();
        ValueGenerator generator=new ValueGenerator();

        for(int i = 0; i<attributesToBeGenerated.size()-1; i++){
            String attr=attributesToBeGenerated.get(i);
            String next=attributesToBeGenerated.get(i+1);
            if (trigger){
                String[] attrs = attr.split("\\.");
                String[] nexts = next.split("\\.");
                attributes.add(attr);
                if (!attrs[0].equals(nexts[0])){
                    trigger=false;
                    data = generator.generateComplex(attributes, attributeElement);
                    generatedData.put(attr, data.get(attr));
                    attributes=new ArrayList<String>();
                    if (i==attributesToBeGenerated.size()-1){
                        data = generator.generate(next, attributeElement.get(next));
                        generatedData.put(attr, data.get(attr));
                    }
                }else if (i==attributesToBeGenerated.size()-1){
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
