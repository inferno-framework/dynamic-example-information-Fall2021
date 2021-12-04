package com.gatech.web;

import com.gatech.services.ExampleGenerator;
import com.gatech.services.Helper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class ImplementationGuideController {
    @GetMapping("/implementationGuide")
    public String getAllImplementationGuide() {
        Helper helper = new Helper();
        List<String> implementationGuide = helper.getAllImplementationGuideName();
        return JSONArray.toJSONString(implementationGuide);
    }

    @GetMapping( "/implementationGuide/{profiles}")
    @ResponseBody
    public String getProfilesByImplementationGuide(@PathVariable("profiles") String impGuide) {
        System.out.println(impGuide);
        Helper helper = new Helper();
        List<String> profiles = helper.getAllImplementationGuideProfile(impGuide);
        if (profiles != null) {
            return JSONArray.toJSONString(profiles);
        } else {
            return "The implementation guide you pass is not supported at the moment";
        }
    }

    @GetMapping( "/implementationGuide/{impGuideName}/data")
    @ResponseBody
    public Serializable getExampleDataByImplementationGuide(@PathVariable("impGuideName") String impGuide) throws IOException, ParseException {
        ExampleGenerator exampleGenerator = new ExampleGenerator();
        JSONObject returnedData = exampleGenerator.generate(impGuide, false, false);
        return Objects.requireNonNullElse(returnedData, "The implementation guide you pass is not supported at the moment");
    }
}
