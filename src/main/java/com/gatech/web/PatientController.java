package com.gatech.web;

import org.json.simple.JSONObject;
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
    public JSONObject getAllPatients() {
        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(
                    new FileReader("src/main/java/com/gatech/data/Aaron697_Brekke496_2fa15bc7-8866-461a-9000-f739e425860a.json"));
            return data;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
