package com.gatech.services.parser;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/*
    This class will sever as an implementation guide parser that will read the guide, and list out all the musthave and mustSupport attribute.
 */
public class ImplementationGuide {

    // TODO: Read implementation guide that is stored locally
    public JSONObject readImplementationGuide(String filePath) {
        try {
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
    public List<String> findValuesInCode(String attribute, String ig) throws IOException {

        List<String> valueset = new ArrayList<>();
        JSONObject impGuideJson = readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/" + ig);
        JSONObject snapshot = (JSONObject) impGuideJson.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");

        for (Object slide : element) {
            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");

            if (id.contains(attribute) && jsonObject2.containsKey("binding")) {
                JSONObject jsonObject3 = (JSONObject) jsonObject2.get("binding");
                String valueset_link = (String) jsonObject3.get("valueSet");
                //valueset.add(valueset_link);
                //grab table content from the link
                Document doc = Jsoup.connect(valueset_link.trim().replace("|", "%7C")).get();
                Element table = doc.select(".codes").first();
                if (table == null) {
                    table = doc.select(".none").first();
                }
                if (table != null) {
                    Element row = table.select("tr").get(2);
                    //List<String> values = List.of(table.text().split(" "));
                    //valueset.addAll(values);
                    Element col = row.select("td").get(0);
                    valueset.add(col.text());
                } else {
                    valueset.add("Intensional value set is not supported");
                }

                return valueset;
            }
        }
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
                        attributes.add(attrs[1]);
                    } else if (attrs.length == 3) {
                        if (attrs[2].contains(":")) {
                            String[] secondAttrs = attrs[2].split("\\:");
                            attributes.add(attrs[1] + secondAttrs[0] +secondAttrs[1]);
                        }
                    }
                }
            }
        }
        return attributes;
    }
}
