package pe.edu.galaxy.logistics.query.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pe.edu.galaxy.logistics.query.document.OrderDocument;
import java.util.Optional;

public interface OrderQueryRepository extends MongoRepository<OrderDocument, String> {
    Optional<OrderDocument> findByOrderId(Long orderId);
}
