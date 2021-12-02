package com.gatech.testing;

import com.gatech.services.parser.Synthea;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HelperTest {

    Synthea synthea = new Synthea();

    public void testSyntheaAttribute() {

        Multimap<String, JSONObject> syntheaAttribute = HashMultimap.create();
        syntheaAttribute  = synthea.findAttributeOnSynthea();

        for (Map.Entry<String, Collection<JSONObject>> entry : syntheaAttribute.asMap().entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

}
