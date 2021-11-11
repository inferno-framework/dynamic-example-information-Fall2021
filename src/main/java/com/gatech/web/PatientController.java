package com.gatech.web;

import com.gatech.services.parser.ImplementationGuide;
import com.gatech.services.SyntheaRecordFetch;
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
            ImplementationGuide impGuideParser = new ImplementationGuide();
            SyntheaRecordFetch syntheaRecordFetch = new SyntheaRecordFetch();
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(
                    new FileReader("src/main/java/com/gatech/data/implementationGuide/us-core-patient.json"));
            JSONObject snapshot = (JSONObject) data.get("snapshot");
            JSONArray element = (JSONArray) snapshot.get("element");
            impGuideParser.findMustSupport(element);
            impGuideParser.findMustHave(element);


            impGuideParser.findResourceType(data);
            //TODO: Get the number of patients from client
            syntheaRecordFetch.fetchPatients(2);
            return data;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
