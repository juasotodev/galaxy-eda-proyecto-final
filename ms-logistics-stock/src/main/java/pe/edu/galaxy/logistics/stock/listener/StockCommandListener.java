package pe.edu.galaxy.logistics.stock.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockCommandListener {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "stock.validate.command")
    public void onValidateStockCommand(String orderId) {
        log.info("Received Validate Stock Command for Order: {}", orderId);

        // Simulating stock logic (always success for now, or random failure)
        boolean stockAvailable = true;

        if (stockAvailable) {
            log.info("Stock available for Order: {}. Sending Success Event.", orderId);
            rabbitTemplate.convertAndSend("stock.validated.queue", orderId);
        } else {
            log.info("Stock NOT available for Order: {}. Sending Failure Event.", orderId);
            rabbitTemplate.convertAndSend("stock.failed.queue", orderId);
        }
    }
}
