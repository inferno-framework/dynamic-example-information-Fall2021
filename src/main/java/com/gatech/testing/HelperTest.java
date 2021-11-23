package com.gatech.testing;

import com.gatech.services.parser.Synthea;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HelperTest {

    Synthea synthea = new Synthea();

    public void testSyntheaAttribute() {

        Map<String, JSONObject> syntheaAttribute = new HashMap<>();
        syntheaAttribute  = synthea.findAttributeOnSynthea();

        for (Map.Entry<String, JSONObject> entry : syntheaAttribute.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

}
