package pe.edu.galaxy.logistics.orchestrator.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.galaxy.logistics.orchestrator.service.SagaService;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SagaEventListener {

    private final SagaService sagaService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @RabbitListener(queues = "order.created.queue")
    public void onOrderCreated(String message) {
        log.info("Received Order Created Event: {}", message);
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(message);
            // Handle cases where the message might be a double-encoded JSON string
            if (node.isTextual()) {
                node = objectMapper.readTree(node.asText());
            }
            String orderNumber = node.get("orderNumber").asText();
            sagaService.handleOrderCreated(orderNumber, message);
        } catch (Exception e) {
            log.error("Error parsing order created event: {}", message, e);
        }
    }

    @RabbitListener(queues = "stock.validated.queue")
    public void onStockValidated(String message) {
        log.info("Received Stock Validated Event: {}", message);
        sagaService.handleStockValidated(message);
    }

    @RabbitListener(queues = "stock.failed.queue")
    public void onStockFailed(String message) {
        log.info("Received Stock Failed Event: {}", message);
        sagaService.handleStockFailed(message);
    }

    @RabbitListener(queues = "order.completed.queue")
    public void onOrderCompleted(String message) {
        log.info("Received Order Completed Event: {}", message);
        sagaService.handleOrderCompleted(message);
    }

    @RabbitListener(queues = "order.cancelled.queue")
    public void onOrderCancelled(String message) {
        log.info("Received Order Cancelled Event: {}", message);
        sagaService.handleOrderCancelled(message);
    }
}
