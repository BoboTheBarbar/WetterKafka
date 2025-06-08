package de.training.wetterkafka.gateway.wettercontroller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class WetterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWetterByName() throws Exception {
        mockMvc.perform(get("/weather/Bonn"))
                .andExpect(status().isOk());
    }
}
