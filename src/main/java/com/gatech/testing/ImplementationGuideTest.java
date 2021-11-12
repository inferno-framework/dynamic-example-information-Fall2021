package com.gatech.testing;

import com.gatech.services.parser.ImplementationGuide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.gatech.services.ValueGenerator;
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
        System.out.println(implementationGuide.findValuesInCode("Patient.address.state","us-core-patient.json"));
        System.out.println(implementationGuide.findValuesInCode("Medication.language","us-core-medication.json"));
        System.out.println(implementationGuide.findResourceType(data));
        ValueGenerator generator=new ValueGenerator();
        System.out.println(generator.generate("Patient.active","us-core-patient.json").toJSONString());
        System.out.println(generator.generate("Medication.language","us-core-patient.json").toJSONString());
        System.out.println(implementationGuide.findAllElements(element));
    }
}
