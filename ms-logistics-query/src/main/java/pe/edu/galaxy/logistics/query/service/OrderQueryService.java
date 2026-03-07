package pe.edu.galaxy.logistics.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.logistics.query.document.OrderDocument;
import pe.edu.galaxy.logistics.query.repository.OrderQueryRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderQueryRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "order:";

    public List<OrderDocument> findAll() {
        return repository.findAll();
    }

    public OrderDocument findByOrderId(Long orderId) {
        String key = CACHE_KEY_PREFIX + orderId;

        try {
            OrderDocument cached = (OrderDocument) redisTemplate.opsForValue().get(key);
            if (cached != null) {
                log.info("Order retrieved from Redis: {}", orderId);
                return cached;
            }
        } catch (Exception e) {
            log.warn("Error reading from Redis", e);
        }

        OrderDocument doc = repository.findByOrderId(orderId)
                .orElse(null);

        if (doc != null) {
            saveToCache(doc);
        }

        return doc;
    }

    public void saveToCache(OrderDocument doc) {
        try {
            String key = CACHE_KEY_PREFIX + doc.getOrderId();
            redisTemplate.opsForValue().set(key, doc, 10, TimeUnit.MINUTES);
            log.info("Order saved to Redis: {}", doc.getOrderId());
        } catch (Exception e) {
            log.warn("Error saving to Redis", e);
        }
    }
}
