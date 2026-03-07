package pe.edu.galaxy.logistics.orchestrator.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaStatus;
import pe.edu.galaxy.logistics.orchestrator.domain.SagaType;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sagas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagaEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private SagaType type;

    @Enumerated(EnumType.STRING)
    private SagaStatus status;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
