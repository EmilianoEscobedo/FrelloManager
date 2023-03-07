package com.laingard.FrelloManager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.security.jwt.TokenUtils;
import com.laingard.FrelloManager.security.services.UserDetailsServiceImpl;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenUtils tokenUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private ProductServiceImpl productService;

    private ProductDto productDto;
    private List<ProductDto> productDtoList;

    @BeforeEach
    public void init(){
        productDto = new ProductDto();
        productDto.setName("testProduct");
        productDto.setPrice(10.0);

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setName("Product Name 2");

        productDtoList = new ArrayList<>();
        productDtoList.add(productDto);
        productDtoList.add(productDto2);
    }

    @Test
    @WithMockUser(username = "admin", authorities= {"ADMIN", "SALES"})
    public void ProductController_SaveProduct_ReturnCreated() throws Exception {
        given(productService.save(any(ProductDto.class))).willReturn(productDto);

        ResultActions response = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "SALES"})
    public void ProductController_FindAll_ReturnOk() throws Exception {
        given(productService.findAll()).willReturn(productDtoList);

        ResultActions response = mockMvc.perform(get("/products"));

        response.andExpect(status().isOk());

        List<ProductDto> responseProducts = objectMapper.readValue(response
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<List<ProductDto>>() {});

        assertEquals(productDtoList.size(), responseProducts.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "SALES"})
    public void ProductController_GetAvailableProducts_ReturnOk() throws Exception {

        given(productService.filterByAvailable()).willReturn(Collections.singletonList(productDto));
        ResultActions response = mockMvc.perform(get("/products/available")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());

        List<ProductDto> responseProducts = objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), new TypeReference<List<ProductDto>>(){});

        assertEquals(1, responseProducts.size());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "SALES"})
    public void ProductController_ChangeProduct_ReturnOk() throws Exception {

        given(productService.update(any(ProductDto.class), anyLong(), anyString())).willReturn(productDto);
        ResultActions response = mockMvc.perform(put("/products/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(status().isOk());

        ProductDto responseProduct = objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), ProductDto.class);

        assertEquals(productDto.getId(), responseProduct.getId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN", "SALES"})
    public void ProductController_DeleteProduct_ReturnOk() throws Exception {

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("Product deleted successfully")));

        verify(productService, times(1)).deleteOne(1L);
    }
}
