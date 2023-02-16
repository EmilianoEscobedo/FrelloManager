package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.FilteredOrderDto;
import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Error: Not enough stock of {product name}, id ({product id})",
                    content = @Content)})
    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @PostMapping()
    public OrderDto save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New order details")
                             @RequestBody OrderDto order){
        return orderService.save(order);
    }

    @Operation(summary = "Get all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content)})
    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @GetMapping()
    public List<OrderDto> getAll() {
        return orderService.findAll();
    }

    @Operation(summary = "Get orders by state and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content)})
    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @GetMapping("/where")
    public FilteredOrderDto filterOrder(@Parameter(description = "State of the orders to filter")
                                            @RequestParam String state,
                                        @Parameter(description = "Initial date of the orders to filter")
                                            @RequestParam String from,
                                        @Parameter(description = "End date of the orders to filter")
                                            @RequestParam String to) {
        return orderService.findByDate(state, from, to);
    }

    @Operation(summary = "Get one order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One order",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Product not found",
                    content = @Content)})
    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public OrderDto getOne(@Parameter(description = "Id of the order to find")
                               @PathVariable Long id) {
        return orderService.findOne(id);
    }

    @Operation(summary = "Update one product by id and attribute")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Attribute not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Error: Not enough stock of {product name}, id ({product id})",
                    content = @Content)})
    @PreAuthorize("hasRole('SALES') or hasRole('ADMIN')")
    @PutMapping("{id}/{attribute}")
    public OrderDto update(@Parameter(description = "Id of the order to update")
                               @PathVariable Long id,
                           @Parameter(description = "Attribute name of the order to update")
                                @PathVariable String attribute,
                           @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order details to be updated")
                                    @RequestBody OrderDto request){
        return orderService.update(request, id, attribute);
    }

    @Operation(summary = "Delete order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Product not found",
                    content = @Content)})
    @PreAuthorize("hasRole('DELIVERY') or hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@Parameter(description = "Id of the order to be deleted")
                                           @PathVariable Long id) {
        orderService.deleteOne(id);
        return ResponseEntity.ok(new MessageResponse("Order deleted successfully"));
    }
}
