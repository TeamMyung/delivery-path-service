package com.sparta.deliverypathservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DeliveryPathServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryPathServiceApplication.class, args);
    }

}
