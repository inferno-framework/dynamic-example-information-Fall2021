package com.gatech.testing;

import com.gatech.services.parser.ImplementationGuide;
import org.apache.catalina.mapper.Mapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ImplementationGuideTest {


    public ImplementationGuideTest() {
    }

    public void testFunctionality() throws IOException, ParseException {
        ImplementationGuide implementationGuide = new ImplementationGuide();

        final JSONObject data = implementationGuide.readImplementationGuide("src/main/java/com/gatech/data/implementationGuide/us-core-patient.json");

        JSONObject snapshot = (JSONObject) data.get("snapshot");
        JSONArray element = (JSONArray) snapshot.get("element");
        System.out.println(implementationGuide.findMustSupport(element));
        System.out.println(implementationGuide.findMustHave(element));
        System.out.println(implementationGuide.findValuesInCode("time"));
        System.out.println(implementationGuide.findResourceType(data));

        System.out.println(implementationGuide.findAllElements(element));
    }
}
