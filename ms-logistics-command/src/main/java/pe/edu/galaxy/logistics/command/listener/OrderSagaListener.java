package pe.edu.galaxy.logistics.command.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.galaxy.logistics.command.service.OrderCommandService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaListener {

    private final OrderCommandService orderCommandService;
    private final org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "order.complete.command")
    public void onOrderCompleteCommand(String orderNumber) {
        log.info("Received Order Complete Command for Order: {}", orderNumber);
        orderCommandService.updateStatusByOrderNumber(orderNumber, "ACCEPTED");
        rabbitTemplate.convertAndSend("order.completed.queue", orderNumber);
    }

    @RabbitListener(queues = "order.cancel.command")
    public void onOrderCancelCommand(String orderNumber) {
        log.info("Received Order Cancel Command for Order: {}", orderNumber);
        orderCommandService.updateStatusByOrderNumber(orderNumber, "REJECTED");
        rabbitTemplate.convertAndSend("order.cancelled.queue", orderNumber);
    }
}
