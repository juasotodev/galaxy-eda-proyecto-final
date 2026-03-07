package pe.edu.galaxy.logistics.command.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.galaxy.logistics.command.config.RabbitMQConfig;
import pe.edu.galaxy.logistics.command.entity.MessageEntity;
import pe.edu.galaxy.logistics.command.repository.MessageRepository;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxJob {

    private final MessageRepository messageRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        List<MessageEntity> pending = messageRepository.findPendingMessages();

        for (MessageEntity message : pending) {
            try {
                String routingKey = message.getPayload().contains("\"status\":\"PENDING\"") ? "order.created"
                        : RabbitMQConfig.ROUTING_KEY;
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        routingKey,
                        message.getPayload());
                messageRepository.markAsProcessed(message.getId());
                log.info("Event sent to RabbitMQ: {}", message.getId());
            } catch (Exception e) {
                log.error("Error sending event {}", message.getId(), e);
            }
        }
    }
}
