package pe.edu.galaxy.logistics.command.service;

import pe.edu.galaxy.logistics.command.dto.OrderRequest;

public interface OrderCommandService {
    void create(OrderRequest request);

    void updateStatus(Long id, String newStatus);

    void updateStatusByOrderNumber(String orderNumber, String newStatus);
}
