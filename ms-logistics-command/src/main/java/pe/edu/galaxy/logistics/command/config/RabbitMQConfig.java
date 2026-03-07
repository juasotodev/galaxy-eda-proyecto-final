package pe.edu.galaxy.logistics.command.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "orders.exchange";
    public static final String QUEUE = "orders.updated.queue";
    public static final String ROUTING_KEY = "order.updated";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue("order.created.queue");
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder.bind(orderCreatedQueue()).to(exchange()).with("order.created");
    }

    @Bean
    public Queue orderCompleteQueue() {
        return new Queue("order.complete.command");
    }

    @Bean
    public Binding completeBinding() {
        return BindingBuilder.bind(orderCompleteQueue()).to(exchange()).with("order.complete");
    }

    @Bean
    public Queue orderCancelQueue() {
        return new Queue("order.cancel.command");
    }

    @Bean
    public Binding cancelBinding() {
        return BindingBuilder.bind(orderCancelQueue()).to(exchange()).with("order.cancel");
    }
}
