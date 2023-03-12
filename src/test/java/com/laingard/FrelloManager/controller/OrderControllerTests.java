package com.laingard.FrelloManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laingard.FrelloManager.dto.FilteredOrderDto;
import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.dto.OrderProductDto;
import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.security.jwt.TokenUtils;
import com.laingard.FrelloManager.security.services.UserDetailsServiceImpl;
import com.laingard.FrelloManager.service.Impl.OrderServiceImpl;
import com.laingard.FrelloManager.service.Impl.ProductServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenUtils tokenUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private ProductServiceImpl productService;
    private FilteredOrderDto filteredOrderDto;
    private OrderDto orderDto;
    private OrderProductDto orderProductDto;
    private ProductDto productDto;
    private List<OrderDto> orderDtoList;

    @BeforeEach
    public void init (){
        productDto = new ProductDto();
        productDto.setName("French Fries");
        productDto.setId(1L);
        productDto.setQuantity(30.0);
        productDto.setPrice(10.0);

        orderProductDto = new OrderProductDto();
        orderProductDto.setId(1L);
        orderProductDto.setName("French Fries");
        orderProductDto.setQuantity(1.0);

        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        orderProductDtoList.add(orderProductDto);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setClientName("John Doe");
        orderDto.setClientAddress("Siempre Viva 123");
        orderDto.setClientPhone("22113044401");
        orderDto.setState("COOKING");
        orderDto.setProducts(orderProductDtoList);
        orderDto.setTotalPrice(10.0);
        orderDto.setTimeStamp("2023-02-17T19:36:12.390241983");

        orderDtoList = new ArrayList<>();
        orderDtoList.add(orderDto);

        filteredOrderDto = new FilteredOrderDto();
        filteredOrderDto.setOrders(orderDtoList);
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "SALES"})
    public void OrderController_SaveOrder_ReturnCreated() throws Exception {
        given(orderService.save(any(OrderDto.class))).willReturn(orderDto);

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());;;
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "SALES"})
    public void OrderController_GetAll_ReturnOk() throws Exception {
        given(orderService.findAll()).willReturn(orderDtoList);

        ResultActions response = mockMvc.perform(get("/orders"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$[0].clientName", Matchers.is(orderDto.getClientName())))
                .andExpect(jsonPath("$[0].state", Matchers.is(orderDto.getState())));
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "SALES"})
    public void OrderController_FilterOrder_ReturnOk() throws Exception {

        given(orderService.findByDate(anyString(), anyString(), anyString())).willReturn(filteredOrderDto);
        ResultActions response = mockMvc.perform(get("/orders/where")
                .param("state", "COOKING")
                .param("from", "2023-01-01")
                .param("to", "2023-12-31"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.orders", hasSize(1)))
                .andExpect(jsonPath("$.orders[0].id", Matchers.is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.orders[0].clientName", Matchers.is(orderDto.getClientName())))
                .andExpect(jsonPath("$.orders[0].state", Matchers.is(orderDto.getState())));
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "SALES"})
    public void OrderController_GetOne_ReturnOk() throws Exception {
        given(orderService.findOne(anyLong())).willReturn(orderDto);

        ResultActions response = mockMvc.perform(get("/orders/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.clientName", Matchers.is(orderDto.getClientName())))
                .andExpect(jsonPath("$.state", Matchers.is(orderDto.getState())));
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "SALES"})
    public void OrderController_Update_ReturnOk() throws Exception {
        given(orderService.update(any(OrderDto.class), anyLong(), anyString())).willReturn(orderDto);

        ResultActions response = mockMvc.perform(put("/orders/1/state")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.clientName", Matchers.is(orderDto.getClientName())))
                .andExpect(jsonPath("$.state", Matchers.is(orderDto.getState())));
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "DELIVERY"})
    public void OrderController_DeleteOne_ReturnOk() throws Exception {
        ResultActions response = mockMvc.perform(delete("/orders/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
