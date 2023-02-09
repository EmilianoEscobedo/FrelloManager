package com.laingard.FrelloManager.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.dto.OrderProductDto;
import com.laingard.FrelloManager.enumeration.EState;
import com.laingard.FrelloManager.exception.CantBeEmptyException;
import com.laingard.FrelloManager.exception.NotFoundException;
import com.laingard.FrelloManager.mapper.OrderMapper;
import com.laingard.FrelloManager.model.*;
import com.laingard.FrelloManager.repository.OrderRepository;
import com.laingard.FrelloManager.repository.ProductRepository;
import com.laingard.FrelloManager.repository.StateRepository;
import com.laingard.FrelloManager.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StateRepository stateRepository;

    @Transactional
    @Override
    public Order save(OrderDto order) {
        Order nOrder = orderMapper.toEntity(order);
        State orderState = stateRepository.findByName(EState.COOKING)
                .orElseThrow(() -> new RuntimeException("Error: State is not found"));
        nOrder.setState(orderState);
        processProductList(order, nOrder);
        return orderRepository.save(nOrder);
    }

    @Override
    public List<OrderDto> findAll() {
        return orderMapper.toDtoList(orderRepository.findAll());
    }

    @Override
    public OrderDto findOne(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> (new NotFoundException("Error: Order id does not exist")));
        return orderMapper.toDto(order);
    }

    @Override
    public void deleteOne(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> (new NotFoundException("Error: Order id does not exist")));
        List<OrderProduct> productsOrderList = order.getProducts();
        for (OrderProduct productInOrder : productsOrderList){
            updateStock(productInOrder.getProduct().getId(), productInOrder.getQuantity());
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Order update(OrderDto request, Long id, String attribute) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> (new NotFoundException("Error: Order id does not exist")));
        switch (attribute){
            case "name" -> {
                if (request.getClientName() == null || request.getClientName().equals(""))
                    throw new CantBeEmptyException("Error: Client name cant be empty");
                order.setClientName(request.getClientName());
            }
            case "address" -> {
                if (request.getClientAddress() == null || request.getClientName().equals(""))
                    throw new CantBeEmptyException("Error: Client address cant be empty");
                order.setClientAddress(request.getClientAddress());
            }
            case "phone" -> {
                if (request.getClientPhone() == null || request.getClientPhone().equals(""))
                    throw new CantBeEmptyException("Error: Client phone cant be empty");
                order.setClientPhone(request.getClientPhone());
            }
            case "products" -> {
                if (request.getProducts() == null || request.getProducts().isEmpty())
                    throw new CantBeEmptyException("Error: New products cant be empty");
                updateOrderProducts(order, request.getProducts());
            }
            default -> {
                throw new NotFoundException("Error: Attribute not found");
            }
        }
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public void toDelivery(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> (new NotFoundException("Error: Order id does not exist")));
        State orderState = stateRepository.findByName(EState.DELIVERY)
                .orElseThrow(() -> new RuntimeException("Error: State is not found"));
        order.setState(orderState);
        orderRepository.save(order);
    }

    @Override
    public void toSalesBook(Long id) {
    }

    @Transactional
    @Override
    public void delivered(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> (new NotFoundException("Error: Order id does not exist")));
        State orderState = stateRepository.findByName(EState.DELIVERED)
                .orElseThrow(() -> new RuntimeException("Error: State is not found"));
        order.setState(orderState);
        orderRepository.save(order);
    }

    @Transactional
    public void processProductList(OrderDto dto, Order order) {
        List<OrderProduct> orderProductsList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderProductDto> productsInOrderDto = objectMapper.convertValue(dto.getProducts(), new TypeReference<List<OrderProductDto>>() {});
        for (OrderProductDto productDto : productsInOrderDto){
            Product productInStock = productRepository.findById(productDto.getId()).orElseThrow(
                    () -> (new NotFoundException("Product with id " + productDto.getId() + " not found")));
            orderProductsList.add(new OrderProduct(order, productInStock, productDto.getQuantity()));
            updateStock(productDto.getId(), -productDto.getQuantity());
        }
        order.setProducts(orderProductsList);
        order.setTotalPrice(calculateTotalPrice(order));
    }
    @Transactional
    private void updateOrderProducts(Order order, List<OrderProductDto> newProducts) {
        Map<Long, OrderProductDto> newProductsMap = newProducts.stream().collect(Collectors.toMap(OrderProductDto::getId, Function.identity()));
        for (Iterator<OrderProduct> iterator = order.getProducts().iterator(); iterator.hasNext();) {
            OrderProduct orderProduct = iterator.next();
            OrderProductDto newProduct = newProductsMap.get(orderProduct.getProduct().getId());
            if (newProduct != null) {
                double quantityDifference = newProduct.getQuantity() - orderProduct.getQuantity();
                orderProduct.setQuantity(newProduct.getQuantity());
                updateStock(orderProduct.getProduct().getId(), -quantityDifference);
                newProductsMap.remove(orderProduct.getProduct().getId());
            } else {
                updateStock(orderProduct.getProduct().getId(), orderProduct.getQuantity());
                iterator.remove();
            }
        }
        for (OrderProductDto newProduct : newProductsMap.values()) {
            Product product = productRepository.findById(newProduct.getId()).orElse(null);
            if (product == null) {
                throw new NotFoundException("Error: Product not found");
            }
            updateStock(product.getId(), -newProduct.getQuantity());
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(newProduct.getQuantity());
            order.getProducts().add(orderProduct);
        }
        order.setTotalPrice(calculateTotalPrice(order));
    }
    @Transactional
    private void updateStock(long productId, double quantity) {
        Product productInStock = productRepository.findById(productId).orElse(null);
        productInStock.setQuantity(productInStock.getQuantity() + quantity);
        if (productInStock.getQuantity() < 0)
            throw new NotFoundException("Error: Not enough stock of " + productInStock.getName() + " (id " + productInStock.getId() + ")");
        productRepository.save(productInStock);
    }
    private double calculateTotalPrice(Order order) {
        return order.getProducts().stream().mapToDouble(product -> product.getQuantity() * product.getProduct().getPrice()).sum();
    }
}
