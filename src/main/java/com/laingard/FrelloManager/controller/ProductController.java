package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.service.ProductService;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody ProductDto newProduct) {
        productService.save(newProduct);
        return ResponseEntity.ok(new MessageResponse("Product saved successfully"));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @GetMapping()
    public List<ProductDto> findAll(){
        return productService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @GetMapping("/{id}")
    public ProductDto findOne(@PathVariable Long id){
        return productService.findOne(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @PutMapping("/{id}/{attribute}")
    public ResponseEntity<?> changeProduct(@RequestBody ProductDto productDto,
                                           @PathVariable Long id,
                                           @PathVariable String attribute){
        productService.update(productDto, id, attribute);
        return ResponseEntity.ok(new MessageResponse("Product updated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id){
        productService.deleteOne(id);
        return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
    }

}
