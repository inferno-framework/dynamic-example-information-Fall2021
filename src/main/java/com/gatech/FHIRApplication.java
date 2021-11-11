package com.gatech;

import com.gatech.services.Helper;
import com.gatech.services.parser.Synthea;
import com.gatech.testing.ImplementationGuideTest;
import com.gatech.web.PatientController;
import com.gatech.testing.SyntheaTest;
import org.json.simple.parser.ParseException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FHIRApplication {

    public static void main(String[] args) throws IOException, ParseException {
        //SpringApplication.run(FHIRApplication.class, args);

        // Just an example to show missing attributes in synthea for patient profile
        Helper helper = new Helper();
        Map<String, List<String>> missingAttr = helper.findMissingAttributeByProfile();

        System.out.println("Missing values in patient:");
        for (Map.Entry<String, List<String>> entry : missingAttr.entrySet()) {
            String key = entry.getKey();
            List<String> missingValues = entry.getValue();

            for (String missVal : missingValues) {
                System.out.println(missVal + ",");
                System.out.println("---");
            }
        }
    }

}
