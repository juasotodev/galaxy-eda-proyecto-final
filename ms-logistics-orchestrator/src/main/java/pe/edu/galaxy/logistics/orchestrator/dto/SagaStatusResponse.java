package pe.edu.galaxy.logistics.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaStatus;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaType;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagaStatusResponse {
    private UUID sagaId;
    private SagaType sagaType;
    private SagaStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
}
