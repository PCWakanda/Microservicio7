package com.example.microservicio7.RabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue aulaQueue() {
        return new Queue("aulaQueue", true);
    }

    @Bean
    public Queue campusQueue() {
        return new Queue("campusQueue", true);
    }
}