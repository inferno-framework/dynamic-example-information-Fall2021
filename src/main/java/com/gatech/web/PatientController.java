package com.gatech.web;

import com.gatech.services.parser.ImplementationGuide;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class PatientController {

    /**
     * Dumping a single json file to the browser as an API
     * @return
     */
    @GetMapping("/patients")
    public static JSONObject getAllPatients() {
        Object object;
        try {
            ImplementationGuide implementationGuide = new ImplementationGuide();
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(
                    new FileReader("src/main/java/com/gatech/impGuide/us-core.json"));
            JSONObject snapshot = (JSONObject) data.get("snapshot");
            JSONArray element = (JSONArray) snapshot.get("element");
            implementationGuide.findMustSupport(element);
            implementationGuide.findMustHave(element);
            implementationGuide.findValuesInCode("Patient.address.state");
            implementationGuide.findResourceType(data);
            return data;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
