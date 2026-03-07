package pe.edu.galaxy.logistics.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.logistics.query.document.OrderDocument;
import pe.edu.galaxy.logistics.query.service.OrderQueryService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/queries/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @GetMapping
    public List<OrderDocument> findAll() {
        return orderQueryService.findAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDocument> findByOrderId(@PathVariable Long orderId) {
        OrderDocument doc = orderQueryService.findByOrderId(orderId);
        return doc != null ? ResponseEntity.ok(doc) : ResponseEntity.notFound().build();
    }
}
