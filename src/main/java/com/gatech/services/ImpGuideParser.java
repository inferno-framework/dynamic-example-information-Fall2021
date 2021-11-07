package com.gatech.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            long mustHaveValue = (long) jsonObject2.get("min");

            if(mustHaveValue > 0){
                mustHave.add(id);
            }
        }
        System.out.println("Must Have Elements are " + mustHave);
        return mustHave;
    }

    // TODO: Check for the "code" datatype, and list out all the supported values from the resources it is binded to
    public List<String> findValuesInCode(String attribute) throws IOException {

        List<String> valueset = new ArrayList<>();
        JSONObject impGuideJson = readImplementationGuide("src/main/java/com/gatech/impGuide/us-core.json");
        JSONObject snapshot = (JSONObject) impGuideJson.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");

        for (Object slide : element) {
            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");

            if (id.equals(attribute) && jsonObject2.containsKey("binding")) {
                JSONObject jsonObject3 = (JSONObject) jsonObject2.get("binding");
                String valueset_link = (String) jsonObject3.get("valueSet");
                //valueset.add(valueset_link);
                //grab table content from the link
                Document doc = Jsoup.connect(valueset_link).get();
                Element table = doc.select(".codes").first();
                if (table==null){
                    table = doc.select(".none").first();
                }
                if (table != null) {
                    Element row = table.select("tr").get(2);
                    //List<String> values = List.of(table.text().split(" "));
                    //valueset.addAll(values);
                    Element col=row.select("td").get(0);
                    valueset.add(col.text());
                }else{
                    valueset.add("Intensional value set is not supported");
                }

                System.out.println(valueset);
                return valueset;
            }
        }
        valueset.add("Intensional value set is not supported");
        return valueset;
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
