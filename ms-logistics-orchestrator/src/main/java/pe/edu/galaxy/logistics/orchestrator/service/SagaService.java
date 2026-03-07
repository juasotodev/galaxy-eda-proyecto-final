package pe.edu.galaxy.logistics.orchestrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaStateMachine;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaStatus;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaType;
import pe.edu.galaxy.logistics.orchestrator.entity.SagaEntity;
import pe.edu.galaxy.logistics.orchestrator.event.SagaStateChangedEvent;
import pe.edu.galaxy.logistics.orchestrator.repository.SagaRepository;
import pe.edu.galaxy.logistics.orchestrator.publisher.SagaCommandPublisher;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SagaService {

    private final SagaRepository sagaRepository;
    private final SagaStateMachine stateMachine;
    private final SagaCommandPublisher commandPublisher;
    private final ApplicationEventPublisher eventPublisher;

    public void handleOrderCreated(String orderId, String payload) {
        log.info("Starting Order Creation Saga for Order {}", orderId);

        SagaEntity saga = SagaEntity.builder()
                .id(orderId)
                .type(SagaType.ORDER_CREATION)
                .status(SagaStatus.STARTED)
                .payload(payload)
                .build();

        sagaRepository.save(saga);

        // Trigger next step: Stock Validation
        commandPublisher.sendValidateStockCommand(orderId);
    }

    public void handleStockValidated(String sagaId) {
        updateSagaStatus(sagaId, SagaStatus.STOCK_VALIDATED);
        commandPublisher.sendCompleteOrderCommand(sagaId);
    }

    public void handleStockFailed(String sagaId) {
        updateSagaStatus(sagaId, SagaStatus.ORDER_FAILED);
        updateSagaStatus(sagaId, SagaStatus.COMPENSATING);
        commandPublisher.sendCancelOrderCommand(sagaId);
    }

    public void handleOrderCompleted(String sagaId) {
        updateSagaStatus(sagaId, SagaStatus.ORDER_COMPLETED);
    }

    public void handleOrderCancelled(String sagaId) {
        updateSagaStatus(sagaId, SagaStatus.COMPENSATED);
    }

    private void updateSagaStatus(String sagaId, SagaStatus nextStatus) {
        SagaEntity saga = sagaRepository.findById(sagaId)
                .orElseThrow(() -> new RuntimeException("Saga not found: " + sagaId));

        if (saga.getStatus() == nextStatus) {
            log.info("Saga {} is already in status {}", sagaId, nextStatus);
            return;
        }

        stateMachine.validateTransition(saga.getType(), saga.getStatus(), nextStatus);

        SagaStatus previousStatus = saga.getStatus();
        saga.setStatus(nextStatus);
        sagaRepository.save(saga);
        log.info("Saga {} moved to status {}", sagaId, nextStatus);

        // Publicar evento para alertas (Email/SMS)
        eventPublisher.publishEvent(SagaStateChangedEvent.builder()
                .sagaId(sagaId)
                .previousStatus(previousStatus)
                .nextStatus(nextStatus)
                .payload(saga.getPayload())
                .timestamp(LocalDateTime.now())
                .build());
    }
}
