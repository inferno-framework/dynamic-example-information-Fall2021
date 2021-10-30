package com.gatech.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    This class will sever as an implementation guide parser that will read the guide, and list out all the musthave and mustSupport attribute.
 */
public class ImpGuideParser {

    // TODO: Read implementation guide that is stored locally
    public JSONObject readImplementationGuide(String filePath) {
        try {
            ImpGuideParser impGuideParser = new ImpGuideParser();
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
        List<String> values = new ArrayList<>();
        JSONObject impGuideJson = readImplementationGuide("src/main/java/com/gatech/impGuide/us-core.json");
        JSONObject snapshot = (JSONObject) impGuideJson.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");
        for (Object slide : element) {

            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");
            if (id.equals(attribute) && jsonObject2.containsKey("binding")) {
                JSONObject jsonObject3 = (JSONObject) jsonObject2.get("binding");
                String valueset = (String) jsonObject3.get("valueSet");


                Document doc = Jsoup.connect(valueset).get();
                Element masthead = doc.select(".codes").first();

                values.add(valueset);
                if (masthead!=null) {
                    values.add(masthead.text());
                }
                System.out.println(values);
                return values;
            }
        }

        /*File input = new File("src/main/java/com/gatech/datatypes/hl7.org_fhir_R4_datatypes.html");
        Document doc = Jsoup.parse(input, "UTF-8", "http://hl7.org/fhir/R4/datatypes.html");
        Element masthead = doc.select("#"+attribute).first();
        System.out.println("Regex: "+masthead.text());
        return masthead.text();*/
        return null;
    }

    // TODO: Find which resource does the implementation guide
    // NOTES: This can be found using  "kind" value and under the snapshot block.
    // This resource will be used to dynamically create the API and render the data
    public String findResourceType(JSONObject impGuideJson) {
        String kind = (String) impGuideJson.get("type");
        System.out.println("The resource is " + kind);
        return kind;
    }

}
