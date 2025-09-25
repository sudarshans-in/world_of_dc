package org.dcoffice.cachar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for Cachar Complaint Management System
 *
 * This configuration allows cross-origin requests from web browsers,
 * enabling frontend applications to communicate with the API.
 *
 * @author District Cachar IT Team
 */
@Configuration
public class CorsConfig {

    /**
     * Configure CORS mapping for all API endpoints
     *
     * @return WebMvcConfigurer with CORS settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        // Allow requests from any origin during development
                        // In production, specify exact domains like:
                        // .allowedOrigins("https://cachar.assam.gov.in", "https://complaints.cachar.gov.in")
                        .allowedOriginPatterns("*")

                        // Allow common HTTP methods
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")

                        // Allow common headers
                        .allowedHeaders("*")

                        // Allow credentials (cookies, authorization headers)
                        .allowCredentials(true)

                        // Cache preflight requests for 1 hour
                        .maxAge(3600);

                // Special CORS configuration for file uploads
                registry.addMapping("/api/files/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

                // CORS for actuator endpoints (monitoring)
                registry.addMapping("/actuator/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}