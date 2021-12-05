package com.gatech.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatech.services.parser.ImplementationGuide;
import com.gatech.services.parser.Synthea;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExampleGenerator {

    public void generate(String ig) throws IOException, ParseException {
        ImplementationGuide implementationGuide = new ImplementationGuide();

        Synthea synthea = new Synthea();
        Multimap<String, JSONObject> resourceAndJSON = synthea.findAttributeOnSynthea();
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        List<String> keysFetchedFromSynthea=new ArrayList<String>();
        File[] files = new File("src/main/java/com/gatech/data/implementationGuide/"+ig).listFiles(File::isFile);

        JSONObject example = new JSONObject();

        JSONParser parser = new JSONParser();
        for(File profile : files != null ? files : new File[0]){
            JSONObject data = implementationGuide.readImplementationGuide(profile.getAbsolutePath());
            String resourceType = implementationGuide.findResourceType(data);
            if(resourceAndJSON.get(resourceType).isEmpty()){
                String fullUrl = String.format("urn:uuid:%s", UUID.randomUUID());
                JSONObject request = (JSONObject) parser.parse(String.format("{\"method\": \"POST\",\"url\": \"%s\"}", resourceType));
                JSONObject generatedData = generateDataForProfile(data);
                JSONObject finalData = new JSONObject();
                finalData.put("fullUrl", fullUrl);
                finalData.put("resource", generatedData);
                finalData.put("request", request);
                items.add(finalData);
            }
            else if (keysFetchedFromSynthea.contains(resourceType));
            else{
                items.addAll(resourceAndJSON.get(resourceType));
                keysFetchedFromSynthea.add(resourceType);
            }

        }
        example.put("entry", items);
        example.put("type", "transaction");
        example.put("resourceType", "Bundle");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson=gson.toJson(example);
        // writing the JSONObject into a file
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            FileWriter fileWriter = new FileWriter("src/main/java/com/gatech/data/generatedExample/"+ig+" "+timeStamp+".json");
            fileWriter.write(prettyJson);
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(prettyJson);
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
                    String[] temp = attr.split("\\:");
                    generatedData.put(temp[temp.length-1], data.get(temp[temp.length-1]));
                    attributes=new ArrayList<String>();
                    if (i==allAttributesOnImpGuide.size()-1){
                        data = generator.generate(next, attributeElement.get(next));
                        String[] temp1 = attr.split("\\:");
                        generatedData.put(temp[temp.length-1], data.get(temp[temp.length-1]));
                    }
                }else if (i==allAttributesOnImpGuide.size()-1){
                    attributes.add(next);
                    data = generator.generateComplex(attributes, attributeElement);
                    String[] temp = attr.split("\\:");
                    generatedData.put(temp[temp.length-1], data.get(temp[temp.length-1]));
                }
            }else{
                if (next.contains(attr) ){
                    trigger=true;
                }else{
                    String[] temp = attr.split("\\:");
                    if (attr.contains(".")){
                        List<String> temp2=new ArrayList<String>();
                        temp2.add(attr);
                        data = generator.generateComplex(temp2, attributeElement);
                        generatedData.put(temp[temp.length-1].split("\\.")[0], data.get(temp[temp.length-1].split("\\.")[0]));
                    }else{
                        data = generator.generate(attr, attributeElement.get(attr));
                        generatedData.put(temp[temp.length-1], data.get(temp[temp.length-1]));
                    }


                }
            }
        }
        return generatedData;
    }
}
