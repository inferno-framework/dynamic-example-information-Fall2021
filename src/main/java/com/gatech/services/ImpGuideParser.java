package com.gatech.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/*
    This class will sever as an implementation guide parser that will read the guide, and list out all the musthave and mustSupport attribute.
 */
public class ImpGuideParser {

    // TODO: Read implementation guide that is stored locally
    public JSONArray readImplementationGuide(String filePath) {
        return null;
    }

    // TODO: List out mustSupport and mustHave element from JSON
    public List<String> findMustSupport(JSONArray impGuideJson) {
        List<String> mustSupport = new ArrayList<>();

        for (Object slide : impGuideJson) {

            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");
            Boolean mustSupportValue = (Boolean) jsonObject2.get("mustSupport");

            if(mustSupportValue != null && mustSupportValue){
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

            if(mustHaveValue != null && mustHaveValue){
                mustHave.add(id);
            }
        }
        System.out.println("Must Have Elements are " + mustHave);
        return mustHave;
    }

    // TODO: Check for the "code" datatype, and list out all the supported values from the resources it is binded to
    public List<String> findValuesInCode(String attribute){
        return null;
    }

    // TODO: Find which resource does the implementation guide
    // NOTES: This can be found using  "kind" value and under the snapshot block.
    // This resource will be used to dynamically create the API and render the data
    public String findResourceType(JSONArray impGuideJson) {
        return null;
    }

}
