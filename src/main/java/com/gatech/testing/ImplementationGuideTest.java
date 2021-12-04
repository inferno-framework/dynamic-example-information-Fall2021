package com.gatech.testing;

import com.gatech.services.parser.ImplementationGuide;
import com.gatech.services.Helper;
import org.hibernate.cfg.annotations.ArrayBinder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.gatech.services.ValueGenerator;
import com.gatech.services.ExampleGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplementationGuideTest {


    public ImplementationGuideTest() {
    }

    public void testFunctionality() throws IOException, ParseException {
        ImplementationGuide implementationGuide = new ImplementationGuide();

        final JSONObject data = implementationGuide.readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/us-core/us-core-patient.json");

        JSONObject snapshot = (JSONObject) data.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");
        List<String> allAttributesOnImpGuide = implementationGuide.findAllElements(element);

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
        /*System.out.println(implementationGuide.findMustSupport(element));
        System.out.println(implementationGuide.findMustHave(element));
        System.out.println(implementationGuide.findValuesInCode("Patient.address.state","us-core-patient.json"));
        System.out.println(implementationGuide.findValuesInCode("Medication.language","us-core-medication.json"));
        System.out.println(implementationGuide.findResourceType(data));*/
        ValueGenerator generator=new ValueGenerator();
        Helper h=new Helper();
        Map<String, List<String>> m=h.findMissingAttributeByProfile("src/main/java/com/gatech/data/implementationGuide/us-core/us-core-patient.json");
        System.out.println(generator.generate("gender", attributeElement.get("gender")));

        final JSONObject data1 = implementationGuide.readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/us-core/us-core-condition.json");

        JSONObject snapshot1 = (JSONObject) data1.get("snapshot");
        JSONArray element1 = (JSONArray) snapshot1.get("element");
        List<String> allAttributesOnImpGuide1 = implementationGuide.findAllElements(element1);

        Map<String, JSONObject> attributeElement1 = new HashMap<>();

        for (String attribute:allAttributesOnImpGuide1) {
            for (Object slide : element1) {
                JSONObject jsonObject2 = (JSONObject) slide;
                String id = (String) jsonObject2.get("id");

                if (id.contains(attribute)) {
                    attributeElement1.put(attribute, jsonObject2);
                    break;
                }
            }
        }
        System.out.println(generator.generate("category", attributeElement1.get("category")));
        System.out.println(implementationGuide.findAllElements(element));

        //List<String> test= (List<String>) m.values().toArray()[0];
        //int index=test.indexOf("address.id");
        //System.out.println(test.subList(index,index+12));
        //System.out.println(generator.generateComplex(test.subList(index,index+12),"us-core-patient.json"));
        ExampleGenerator exampleGenerator = new ExampleGenerator();
        System.out.println("Generated Data for us-core Implementation Guide");
        System.out.println(exampleGenerator.generate("us-core"));
        System.out.println("Generated Data for qi-core Implementation Guide");
        System.out.println(exampleGenerator.generate("qi-core"));
        System.out.println("Generated Data for ips Implementation Guide");
        System.out.println(exampleGenerator.generate("ips"));


    }
}
