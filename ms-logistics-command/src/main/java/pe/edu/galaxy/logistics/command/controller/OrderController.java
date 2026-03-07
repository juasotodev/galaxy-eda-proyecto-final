package pe.edu.galaxy.logistics.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.galaxy.logistics.command.dto.OrderRequest;
import pe.edu.galaxy.logistics.command.service.OrderCommandService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody OrderRequest request) {
        orderCommandService.create(request);
        return ResponseEntity.ok("Order created successfully");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        orderCommandService.updateStatus(id, newStatus);
        return ResponseEntity.ok("Status updated successfully");
    }
}
