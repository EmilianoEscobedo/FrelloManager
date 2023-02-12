package com.laingard.FrelloManager.service.Impl;

import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.exception.AlreadyExistsException;
import com.laingard.FrelloManager.exception.CantBeEmptyException;
import com.laingard.FrelloManager.exception.NotFoundException;
import com.laingard.FrelloManager.mapper.ProductMapper;
import com.laingard.FrelloManager.model.Product;
import com.laingard.FrelloManager.repository.ProductRepository;
import com.laingard.FrelloManager.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDto save(ProductDto product) {
        if (productRepository.existsByName(product.getName().toLowerCase()))
            throw new AlreadyExistsException("Error: Product already exist");
        if (product.getName() == null || product.getName().equals(""))
            throw new CantBeEmptyException("Error: Product name cant be empty");
        if (product.getPrice() == null || product.getPrice() == 0)
            throw new CantBeEmptyException("Error: Product price cant be zero");
        Product nProduct = productMapper.toEntity(product);
        productRepository.save(nProduct);
        return productMapper.toDto(nProduct);
    }

    @Override
    public List<ProductDto> findAll() {
        return productMapper.toDtoList(productRepository.findAll());
    }

    @Override
    public List<ProductDto> filterByAvailable() {
        return productMapper.toDtoList(productRepository.filterByAvailable());

    }

    @Override
    public ProductDto findOne(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Product not found")
        );
        return productMapper.toDto(product);
    }

    @Transactional
    @Override
    public void deleteOne(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Product not found"));
        productRepository.deleteById(id);
    }

    @Transactional
    @Override
    public ProductDto update(ProductDto request, Long id, String attribute) {
        Product product = productRepository.findById(id).
                orElseThrow(()-> new NotFoundException("Product not found"));
        switch (attribute){
            case "name" -> {
                if (request.getName() == null || request.getName().equals(""))
                    throw new CantBeEmptyException("Error: Product new name cant be empty");
                product.setName(request.getName());
            }
            case "quantity" -> {
                if (request.getQuantity() == null)
                    throw new CantBeEmptyException("Error: Price must be a number");
                product.setQuantity(request.getQuantity());
            }
            case "price" -> {
                if (request.getPrice() == null || request.getPrice() == 0)
                    throw new CantBeEmptyException("Error: New price cant be zero");
                product.setPrice(request.getPrice());
            }
            default -> {
                throw new NotFoundException("Error: Attribute not found");
            }
        }
        productRepository.save(product);
        return productMapper.toDto(product);
    }
}
