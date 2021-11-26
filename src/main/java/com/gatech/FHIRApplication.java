package com.gatech;

import com.gatech.services.Helper;
import com.gatech.testing.ImplementationGuideTest;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FHIRApplication {

    public static void main(String[] args) throws IOException, ParseException {
        //SpringApplication.run(FHIRApplication.class, args);

          ImplementationGuideTest testImpGuide = new ImplementationGuideTest();
          testImpGuide.testFunctionality();
        //testImpGuide.testFindAllAttributeInIMPGuide();

//        Synthea syntheaTest = new Synthea();
//        syntheaTest.findAttributeOnSynthea();

        //Helper helper = new Helper();
        //helper.findMissingAttributeByProfile();
    }
}
