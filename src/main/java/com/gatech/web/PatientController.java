package com.gatech.web;

import com.gatech.services.ImpGuideParser;
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
            ImpGuideParser impGuideParser = new ImpGuideParser();
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(
                    new FileReader("src/main/java/com/gatech/impGuide/us-core.json"));
            JSONObject snapshot = (JSONObject) data.get("snapshot");
            JSONArray element = (JSONArray) snapshot.get("element");
            impGuideParser.findMustSupport(element);
            impGuideParser.findMustHave(element);
            impGuideParser.findValuesInCode("Patient.address.state");
            impGuideParser.findValuesInCode("Patient.language");

            impGuideParser.findResourceType(data);
            return data;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
