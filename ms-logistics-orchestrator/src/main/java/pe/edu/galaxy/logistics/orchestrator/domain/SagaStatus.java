package pe.edu.galaxy.logistics.orchestrator.domain;

public enum SagaStatus {
    STARTED,
    STOCK_VALIDATED,
    ORDER_COMPLETED,
    ORDER_FAILED,
    COMPENSATING,
    COMPENSATED
}
