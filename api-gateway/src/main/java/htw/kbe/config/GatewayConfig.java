package htw.kbe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("hello-service", r -> r.path("/hello/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://hello-service"))

                .route("hello-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))

                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))

                .route("auth-service", r -> r.path("/songs/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service"))
                .route("auth-service", r -> r.path("/songLists/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service"))
                .build();
    }

}
