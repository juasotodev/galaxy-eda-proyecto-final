package pe.edu.galaxy.logistics.orchestrator.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SagaCommandPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendValidateStockCommand(String orderId) {
        log.info("Sending Validate Stock Command for Order: {}", orderId);
        rabbitTemplate.convertAndSend("stock.validate.command", orderId);
    }

    public void sendCompleteOrderCommand(String orderId) {
        log.info("Sending Complete Order Command for Order: {}", orderId);
        rabbitTemplate.convertAndSend("order.complete.command", orderId);
    }

    public void sendCancelOrderCommand(String orderId) {
        log.info("Sending Cancel Order Command for Order: {}", orderId);
        rabbitTemplate.convertAndSend("order.cancel.command", orderId);
    }
}
