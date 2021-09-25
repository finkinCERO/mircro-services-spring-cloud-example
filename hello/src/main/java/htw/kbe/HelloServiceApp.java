package htw.kbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HelloServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(HelloServiceApp.class, args);
    }
}
