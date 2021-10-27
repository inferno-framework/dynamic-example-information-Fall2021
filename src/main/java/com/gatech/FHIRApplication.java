package com.gatech;

import com.gatech.web.PatientController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FHIRApplication {

    public static void main(String[] args) {
        SpringApplication.run(FHIRApplication.class, args);
        PatientController.getAllPatients();
    }

}
