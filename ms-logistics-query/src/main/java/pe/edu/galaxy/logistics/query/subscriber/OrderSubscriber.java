package pe.edu.galaxy.logistics.query.subscriber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.galaxy.logistics.query.config.RabbitMQConfig;
import pe.edu.galaxy.logistics.query.document.OrderDocument;
import pe.edu.galaxy.logistics.query.repository.OrderQueryRepository;
import pe.edu.galaxy.logistics.query.service.OrderQueryService;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSubscriber {

    private final OrderQueryRepository repository;
    private final OrderQueryService orderQueryService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleOrderUpdated(String payload) {
        log.info("Received Order Update/Created Event: {}", payload);
        try {
            JsonNode node = objectMapper.readTree(payload);
            // Handle cases where the message might be a double-encoded JSON string
            if (node.isTextual()) {
                node = objectMapper.readTree(node.asText());
            }
            Long orderId = node.get("id").asLong();
            String orderNumber = node.get("orderNumber").asText();
            String status = node.get("status").asText();
            String customer = node.get("customer").asText();
            String product = node.get("product").asText();

            OrderDocument doc = repository.findByOrderId(orderId)
                    .orElse(OrderDocument.builder()
                            .orderId(orderId)
                            .orderNumber(orderNumber)
                            .customer(customer)
                            .product(product)
                            .build());

            doc.setLastStatus(status);
            doc.setLastUpdate(LocalDateTime.now());
            doc.getHistory().add(new OrderDocument.StatusHistory(status, LocalDateTime.now()));

            repository.save(doc);
            orderQueryService.saveToCache(doc);
            log.info("Projection updated for order: {}", orderNumber);

        } catch (Exception e) {
            log.error("Error processing order event", e);
        }
    }
}
