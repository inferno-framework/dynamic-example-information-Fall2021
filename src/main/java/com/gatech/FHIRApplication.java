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

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FHIRApplication {

    public static void main(String[] args) throws IOException, ParseException {
        //SpringApplication.run(FHIRApplication.class, args);

        //ImplementationGuideTest testImpGuide = new ImplementationGuideTest();
        //testImpGuide.testFindAllAttributeInIMPGuide();

        Helper helper = new Helper();
        helper.findMissingAttributeByProfile();

        Synthea syntheaTest = new Synthea();
        syntheaTest.findAttributeOnSynthea();

        PatientController.getAllPatients();
    }

}
