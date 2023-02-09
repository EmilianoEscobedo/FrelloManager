package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody OrderDto order){
        orderService.save(order);
        return ResponseEntity.ok(new MessageResponse("Order created successfully"));
    }

    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @GetMapping()
    public List<OrderDto> getAll() {
        return orderService.findAll();
    }

    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable Long id) {
        return orderService.findOne(id);
    }

    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @PutMapping("{id}/{attribute}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @PathVariable String attribute,
                                    @RequestBody OrderDto request){
        orderService.update(request, id, attribute);
        return ResponseEntity.ok(new MessageResponse("Order updated successfully"));
    }

    @PreAuthorize("hasRole('DELIVERY') or hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        orderService.deleteOne(id);
        return ResponseEntity.ok(new MessageResponse("Order deleted successfully"));
    }

    @PreAuthorize("hasRole('KITCHEN') or hasRole('ADMIN')")
    @PutMapping("toDelivery/{id}")
    public ResponseEntity<?> toDelivery(@PathVariable Long id){
        orderService.toDelivery(id);
        return ResponseEntity.ok(new MessageResponse("Order moved to delivery"));
    }

    @PreAuthorize("hasRole('DELIVERY') or hasRole('ADMIN')")
    @PutMapping("delivered/{id}")
    public ResponseEntity<?> delivered(@PathVariable Long id) {
        orderService.delivered(id);
        return ResponseEntity.ok(new MessageResponse("Order delivered"));
    }


    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @PutMapping("payed/{id}")
    public ResponseEntity<?> payed(@PathVariable Long id) {
        orderService.toSalesBook(id);
        return ResponseEntity.ok(new MessageResponse("Order stored in sales book successfully"));
    }
}
