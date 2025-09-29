package com.withNoa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser
    @Test
    void whenAccessingNoa_thenNoaHomeViewIsReturnedWithDateTime() throws Exception {
        mockMvc.perform(get("/noa"))
                .andExpect(status().isOk())
                .andExpect(view().name("noa-home"))
                .andExpect(model().attributeExists("dateTime"));
    }
}
