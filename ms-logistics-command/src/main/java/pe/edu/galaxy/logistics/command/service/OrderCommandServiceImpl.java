package pe.edu.galaxy.logistics.command.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.galaxy.logistics.command.dto.OrderRequest;
import pe.edu.galaxy.logistics.command.entity.MessageEntity;
import pe.edu.galaxy.logistics.command.entity.OrderEntity;
import pe.edu.galaxy.logistics.command.repository.MessageRepository;
import pe.edu.galaxy.logistics.command.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void create(OrderRequest request) {
        OrderEntity entity = OrderEntity.builder()
                .orderNumber(request.getOrderNumber())
                .customer(request.getCustomer())
                .product(request.getProduct())
                .status("PENDING")
                .build();

        orderRepository.save(entity);
        registerEvent(entity);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String newStatus) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        entity.setStatus(newStatus);
        orderRepository.save(entity);
        registerEvent(entity);
    }

    @Override
    @Transactional
    public void updateStatusByOrderNumber(String orderNumber, String newStatus) {
        OrderEntity entity = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));

        entity.setStatus(newStatus);
        orderRepository.save(entity);
        registerEvent(entity);
    }

    private void registerEvent(OrderEntity entity) {
        try {
            String payload = objectMapper.writeValueAsString(entity);
            MessageEntity message = MessageEntity.builder()
                    .payload(payload)
                    .build();
            messageRepository.save(message);
        } catch (Exception e) {
            log.error("Error serializing event", e);
            throw new RuntimeException("Error registering event");
        }
    }
}
