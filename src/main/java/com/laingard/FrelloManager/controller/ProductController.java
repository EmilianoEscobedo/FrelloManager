package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.service.ProductService;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(summary = "Store a new Product in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product stored successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Error: Product name cant be empty",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Product price cant be zero",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Error: Product already exist",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @PostMapping()
    public ProductDto save(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product details to be stored")
                               @RequestBody ProductDto newProduct) {
        return productService.save(newProduct);
    }

    @Operation(summary = "Get all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Product name cant be empty",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Product price cant be zero",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @GetMapping()
    public List<ProductDto> findAll(){
        return productService.findAll();
    }

    @Operation(summary = "Get all products in stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products in stock",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Product name cant be empty",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Product price cant be zero",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @GetMapping("/available")
    public List<ProductDto> findAvailable(){
        return productService.filterByAvailable();
    }

    @Operation(summary = "Get one product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One Product",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Product not found",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @GetMapping("/{id}")
    public ProductDto findOne(@Parameter(description = "id of product to get") @PathVariable Long id){
        return productService.findOne(id);
    }

    @Operation(summary = "Update one product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Error: Product name cant be empty",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Price must be a number",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: New price cant be zero",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @PutMapping("/{id}/{attribute}")
    public ProductDto changeProduct(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Attribute name to be updated")
                                           @RequestBody ProductDto productDto,
                                    @Parameter(description = "id of product to be updated") @PathVariable Long id,
                                    @Parameter(description = "attribute of product to be updated") @PathVariable String attribute){
        return productService.update(productDto, id, attribute);
    }

    @Operation(summary = "Delete one product by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Product not found",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@Parameter(description = "id of product to be deleted")
            @PathVariable Long id){
        productService.deleteOne(id);
        return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
    }

}
