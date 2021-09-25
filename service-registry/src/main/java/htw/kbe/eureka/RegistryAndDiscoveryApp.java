package htw.kbe.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegistryAndDiscoveryApp {

    public static void main(String[] args) {
        SpringApplication.run(RegistryAndDiscoveryApp.class, args);
    }

}
