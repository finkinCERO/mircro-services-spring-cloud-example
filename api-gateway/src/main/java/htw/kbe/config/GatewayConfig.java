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
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))

                .route("song-service", r -> r.path("/songs/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service"))
                .route("song-service", r -> r.path("/playlists/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://song-service"))

                .route("picture-service", r -> r.path("/pictures/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://picture-service"))
                .build();
    }

}
