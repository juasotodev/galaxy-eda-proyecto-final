package pe.edu.galaxy.logistics.orchestrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.galaxy.logistics.orchestrator.entity.SagaEntity;
import java.util.UUID;

public interface SagaRepository extends JpaRepository<SagaEntity, String> {
}
