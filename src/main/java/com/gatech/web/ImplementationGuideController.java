package com.gatech.web;

import com.gatech.services.Helper;
import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ImplementationGuideController {
    @GetMapping("/implementationGuide")
    public String getAllImplementationGuide() {
        Helper helper = new Helper();
        List<String> implementationGuide = helper.getAllImplementationGuideName();
        return JSONArray.toJSONString(implementationGuide);
    }

    @GetMapping( "/implementationGuide/{impGuide}")
    @ResponseBody
    public String getProfilesByImplementationGuide(@PathVariable("impGuide") String impGuide) {
        System.out.println(impGuide);
        Helper helper = new Helper();
        List<String> profiles = helper.getAllImplementationGuideProfile(impGuide);
        if (profiles != null) {
            return JSONArray.toJSONString(profiles);
        } else {
            return "The implementation guide you pass is not supported at the moment";
        }
    }
}
