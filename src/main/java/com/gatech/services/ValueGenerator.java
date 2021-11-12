package com.gatech.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import com.gatech.services.parser.ImplementationGuide;

public class ValueGenerator {
    HashMap<String,String> primitiveType =new HashMap<String,String>();

    public ValueGenerator() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("src/main/java/com/gatech/datatypes/primitive.txt"));
        while (scanner.hasNextLine()) {
            String[] columns = scanner.nextLine().split(" ");
            primitiveType.put(columns[0],columns[1]);
        }
        System.out.println(primitiveType);

    }

    public JSONObject generate(String attribute, String ig) throws IOException {
        String[] attrs = attribute.split("\\.");
        String attr=attrs[attrs.length-1];
        String type=findType(attr,ig);
        JSONObject item = new JSONObject();
        if (primitiveType.containsKey(type)){
            String value=primitiveType.get(type);
            item.put(attr,value);
        }else{
            ImplementationGuide implementationGuide = new ImplementationGuide();
            List<String> code_values=implementationGuide.findValuesInCode("Medication.language","us-core-medication.json");
            if (code_values.size()!=0) {
                item.put(attr, code_values.get(0));
            }else{
                item.put(attr,"not supported yet");
            }
        }
        return item;
    }

    public String findType(String attribute,String ig){
        JSONObject impGuideJson = readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/"+ig);
        JSONObject snapshot = (JSONObject) impGuideJson.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");

        for (Object slide : element) {
            JSONObject jsonObject2 = (JSONObject) slide;
            String id = (String) jsonObject2.get("id");

            if (id.contains(attribute)) {
                JSONArray types = (JSONArray) jsonObject2.get("type");
                JSONObject type = (JSONObject) types.get(0);
                String type_code = (String) type.get("code");
                return type_code;
            }
        }
        return null;
    }


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

}
