package com.example.microservicio7.RabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue aulaQueue() {
        return new Queue("aulaQueue", true);
    }
}