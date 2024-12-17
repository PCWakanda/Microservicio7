package com.example.microservicio7.RabbitMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

    @RabbitListener(queues = "aulaQueue")
    public void listen(String message) {
        logger.info("Received message from aulaQueue: " + message);
    }
}