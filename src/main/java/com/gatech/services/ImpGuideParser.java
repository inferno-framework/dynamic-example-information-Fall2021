package com.gatech.services;

import org.json.simple.JSONArray;

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

        return null;
    }

    // TODO: List out mustHave element
    public List<String> findMustHave(JSONArray impGuideJson) {

        return null;
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
