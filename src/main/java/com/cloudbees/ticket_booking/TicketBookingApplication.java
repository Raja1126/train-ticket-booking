package com.cloudbees.ticket_booking;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketBookingApplication.class, args);
    }

    // Basic metadata for swagger
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Train Ticket Booking API")
                        .description("API for booking train tickets")
                        .version("1.0"));
    }

}
