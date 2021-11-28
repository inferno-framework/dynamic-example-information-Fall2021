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
        /*System.out.println(implementationGuide.findMustSupport(element));
        System.out.println(implementationGuide.findMustHave(element));
        System.out.println(implementationGuide.findValuesInCode("Patient.address.state","us-core-patient.json"));
        System.out.println(implementationGuide.findValuesInCode("Medication.language","us-core-medication.json"));
        System.out.println(implementationGuide.findResourceType(data));*/
        ValueGenerator generator=new ValueGenerator();
        Helper h=new Helper();
        Map<String, List<String>> m=h.findMissingAttributeByProfile("src/main/java/com/gatech/data/implementationGuide/us-core/us-core-patient.json");
        System.out.println(generator.generate("Patient.active","src/main/java/com/gatech/data/implementationGuide/us-core/us-core-patient.json"));
        System.out.println(generator.generate("Medication.language","src/main/java/com/gatech/data/implementationGuide/us-core/us-core-medication.json"));
        System.out.println(implementationGuide.findAllElements(element));

        //System.out.println(m);
        //List<String> test= (List<String>) m.values().toArray()[0];
        //int index=test.indexOf("address.id");
        //System.out.println(test.subList(index,index+12));
        //System.out.println(generator.generateComplex(test.subList(index,index+12),"us-core-patient.json"));
        ExampleGenerator exampleGenerator = new ExampleGenerator();
        exampleGenerator.generate("us-core");


    }
}
