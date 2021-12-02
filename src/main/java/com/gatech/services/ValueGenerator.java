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
import java.util.*;

import com.gatech.services.parser.ImplementationGuide;

public class ValueGenerator {
    HashMap<String,String> primitiveType =new HashMap<String,String>();

    public ValueGenerator() throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("src/main/java/com/gatech/datatypes/primitive.txt"));
        while (scanner.hasNextLine()) {
            String[] columns = scanner.nextLine().split(" ");
            primitiveType.put(columns[0],columns[1]);
        }
        //System.out.println(primitiveType);

    }

    public JSONObject generate(String attribute, JSONObject element) throws IOException {
        String[] attrs = attribute.split("\\.");
        String attr = attrs[attrs.length - 1];
        JSONObject item = new JSONObject();
        if(element != null) {
            String type = findType(attribute, element);
            ImplementationGuide implementationGuide = new ImplementationGuide();
            List<String> code_values = implementationGuide.findValuesInCode(attribute, element);
            if (code_values.size() != 0 ) {
                if (!primitiveType.containsKey(type)){
                    String[] temp = attr.split("\\:");
                    JSONObject child = new JSONObject();
                    JSONArray childs = new JSONArray();
                    JSONObject child2 = new JSONObject();
                    child2.put("system","http://terminology.hl7.org/CodeSystem/"+temp[temp.length-1]);
                    child2.put("code",code_values.get(0));
                    childs.add(child2);
                    child.put("coding",childs);

                    item.put(temp[temp.length-1], child);
                }else{
                    String[] temp = attr.split("\\:");
                    item.put(temp[temp.length-1], code_values.get(0));
                }
            } else {
                if (primitiveType.containsKey(type)) {
                    String value = primitiveType.get(type);
                    if (value.equals("uuid")) {
                        item.put(attr, UUID.randomUUID());
                    } else if (value.equals("current_date")) {
                        item.put(attr, java.time.LocalDate.now().toString());
                    } else if (value.equals("current_time")) {
                        item.put(attr, java.time.LocalTime.now().toString());
                    } else if (value.equals("current_datetime")) {
                        item.put(attr, java.time.LocalDateTime.now().toString());
                    } else {
                        item.put(attr, value);
                    }
                } else {
                    item.put(attr, "not supported yet");
                }
            }
            return item;
        }
        item.put(attr, "not supported yet");
        return item;
    }

    public JSONObject generateComplex(List<String> attributes, Map<String, JSONObject> attrElement) throws IOException {
        JSONArray ja = new JSONArray();
        for (String attribute:attributes){
            ja.add(generate(attribute, attrElement.get(attribute)));
        }
        JSONObject mainObj = new JSONObject();
        mainObj.put(attributes.get(0).split("\\.")[0], ja);
        return mainObj;
    }

    public String findType(String attribute,JSONObject element){
            String id = (String) element.get("id");

            if (id.contains(attribute)) {
                JSONArray types = (JSONArray) element.get("type");
                if (types!=null){
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
