package pe.edu.galaxy.logistics.orchestrator.event;

import lombok.Builder;
import lombok.Getter;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class SagaStateChangedEvent {
    private final String sagaId;
    private final SagaStatus previousStatus;
    private final SagaStatus nextStatus;
    private final String payload;
    private final LocalDateTime timestamp;
}
