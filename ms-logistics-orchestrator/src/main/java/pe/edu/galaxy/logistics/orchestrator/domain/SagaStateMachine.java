package pe.edu.galaxy.logistics.orchestrator.domain;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class SagaStateMachine {

    private static final Map<SagaStatus, Set<SagaStatus>> ORDER_FLOW = Map.of(
            SagaStatus.STARTED, Set.of(SagaStatus.STOCK_VALIDATED, SagaStatus.ORDER_FAILED, SagaStatus.COMPENSATING),
            SagaStatus.STOCK_VALIDATED, Set.of(SagaStatus.ORDER_COMPLETED, SagaStatus.COMPENSATING),
            SagaStatus.ORDER_FAILED, Set.of(SagaStatus.COMPENSATING),
            SagaStatus.COMPENSATING, Set.of(SagaStatus.COMPENSATED),
            SagaStatus.ORDER_COMPLETED, Set.of(),
            SagaStatus.COMPENSATED, Set.of());

    public void validateTransition(SagaType type, SagaStatus current, SagaStatus next) {
        if (current == next)
            return; // Idempotent

        Map<SagaStatus, Set<SagaStatus>> flow = resolveFlow(type);
        Set<SagaStatus> allowed = flow.get(current);

        if (allowed == null || !allowed.contains(next)) {
            throw new RuntimeException("Invalid Saga State Transition: " + current + " -> " + next);
        }
    }

    private Map<SagaStatus, Set<SagaStatus>> resolveFlow(SagaType type) {
        return switch (type) {
            case ORDER_CREATION -> ORDER_FLOW;
            default -> throw new IllegalArgumentException("Unsupported Saga Type: " + type);
        };
    }
}
