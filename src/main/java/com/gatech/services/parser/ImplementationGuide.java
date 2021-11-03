package com.gatech.services.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    This class will sever as an implementation guide parser that will read the guide, and list out all the musthave and mustSupport attribute.
 */
public class ImplementationGuide {

    // TODO: Read implementation guide that is stored locally
    public JSONObject readImplementationGuide(String filePath) {
        try {
            ImplementationGuide implementationGuide = new ImplementationGuide();
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(
                    new FileReader(filePath));
            return data;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO: List out mustSupport and mustHave element from JSON
    public List<String> findMustSupport(JSONArray impGuideJson) {
        List<String> mustSupport = new ArrayList<>();

        for (Object slide : impGuideJson) {

            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");
            Boolean mustSupportValue = (Boolean) jsonObject2.get("mustSupport");

            if (mustSupportValue != null && mustSupportValue) {
                mustSupport.add(id);
            }
        }
        System.out.println("Must Support Elements are " + mustSupport);
        return mustSupport;
    }

    // TODO: List out mustHave element
    public List<String> findMustHave(JSONArray impGuideJson) {
        List<String> mustHave = new ArrayList<>();

        for (Object slide : impGuideJson) {

            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");
            Boolean mustHaveValue = (Boolean) jsonObject2.get("mustHave");

            if (mustHaveValue != null && mustHaveValue) {
                mustHave.add(id);
            }
        }
        System.out.println("Must Have Elements are " + mustHave);
        return mustHave;
    }

    // TODO: Check for the "code" datatype, and list out all the supported values from the resources it is binded to
    public List<String> findValuesInCode(String attribute) throws IOException {
        List<String> valueset = new ArrayList<>();
        JSONObject impGuideJson = readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/us-core.json");
        JSONObject snapshot = (JSONObject) impGuideJson.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");

        for (Object slide : element) {
            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");

            if (id.equals(attribute) && jsonObject2.containsKey("binding")) {
                JSONObject jsonObject3 = (JSONObject) jsonObject2.get("binding");
                String valueset_link = (String) jsonObject3.get("valueSet");
                valueset.add(valueset_link);
                //grab table content from the link
                Document doc = Jsoup.connect(valueset_link).get();
                Element masthead = doc.select(".codes").first();

                if (masthead != null) {
                    List<String> values = List.of(masthead.text().split(" "));
                    valueset.addAll(values);
                }

                System.out.println(valueset);
                return valueset;
            }
        }
        return null;
    }

    public String findResourceType(JSONObject impGuideJson) {
        String kind = (String) impGuideJson.get("type");
        System.out.println("The resource is " + kind);
        return kind;
    }


    // Parse all the attribute from implementation guide
    public List<String> findAllElements(JSONArray impGuideJson) {
        List<String> attributes = new ArrayList<>();
        for (Object element : impGuideJson) {
            JSONObject jsonObject2 = (JSONObject) element;
            String id = (String) jsonObject2.get("id");

            if (id != null) {
                if (id.contains(".")) {
                    String[] attrs = id.split("\\.");

                    if (attrs.length == 2) {
                        System.out.println(attrs[1]);
                        attributes.add(attrs[1]);
                    } else if (attrs.length == 3) {
                        System.out.println(attrs[1] + "."  + attrs[2]);
                        attributes.add(attrs[1] + "." + attrs[2]);
                    } else {

                    }
                }
            }
        }
        return attributes;
    }
}
