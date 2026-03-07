package pe.edu.galaxy.logistics.orchestrator.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "orders.exchange";

    @Bean
    public org.springframework.amqp.core.TopicExchange exchange() {
        return new org.springframework.amqp.core.TopicExchange(EXCHANGE);
    }

    // Commands (sent by orchestrator)
    @Bean
    public Queue stockValidateQueue() {
        return new Queue("stock.validate.command");
    }

    @Bean
    public Queue orderCompleteQueue() {
        return new Queue("order.complete.command");
    }

    @Bean
    public Queue orderCancelQueue() {
        return new Queue("order.cancel.command");
    }

    // Events (listened by orchestrator)
    @Bean
    public Queue orderCreatedQueue() {
        return new Queue("order.created.queue");
    }

    @Bean
    public org.springframework.amqp.core.Binding createdBinding() {
        return org.springframework.amqp.core.BindingBuilder.bind(orderCreatedQueue()).to(exchange())
                .with("order.created");
    }

    @Bean
    public Queue stockValidatedQueue() {
        return new Queue("stock.validated.queue");
    }

    @Bean
    public Queue stockFailedQueue() {
        return new Queue("stock.failed.queue");
    }

    @Bean
    public Queue orderCompletedQueue() {
        return new Queue("order.completed.queue");
    }

    @Bean
    public Queue orderCancelledQueue() {
        return new Queue("order.cancelled.queue");
    }
}
