package net.pkhapps.dart.modules.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@IntegrationComponentScan
public class DartDispatchModule {

    public static void main(String[] args) {
        SpringApplication.run(DartDispatchModule.class, args);
    }
}
