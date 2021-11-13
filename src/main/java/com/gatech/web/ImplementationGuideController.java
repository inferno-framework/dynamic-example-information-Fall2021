package com.gatech.web;

import com.gatech.services.Helper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ImplementationGuideController {

    @GetMapping("/implementationGuide")
    public String getAllImplementationGuide() {
        Helper helper = new Helper();
        List<String> profiles = helper.getAllImplementationGuideProfile();
        return JSONArray.toJSONString(profiles);
    }

    @GetMapping("/implementationGuide/{profileName}")
    public static JSONObject getImplementationGuideProfileData(@PathVariable String profileName) {
        return null;
    }

    @GetMapping("/implementationGuide/{profileName}/{id}")
    public static JSONObject getImplementationGuideProfileDataById(@PathVariable String profileName, @PathVariable String id) {
        return null;
    }
}
