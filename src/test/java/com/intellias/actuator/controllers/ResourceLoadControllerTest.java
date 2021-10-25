package com.intellias.actuator.controllers;

import static com.intellias.actuator.utils.Constants.CPU_LOAD_REQUEST_PARAM_NAME;
import static com.intellias.actuator.utils.Constants.MEMORY_LOAD_REQUEST_PARAM_NAME;
import static com.intellias.actuator.utils.Constants.SUCCESS_MESSAGE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.intellias.actuator.services.CpuLoadService;
import com.intellias.actuator.services.MemoryLoadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(
        value = ResourceLoadController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE
        )
)
class ResourceLoadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CpuLoadService cpuLoadService;
    @MockBean
    private MemoryLoadService memoryLoadService;


    @Test
    void given50CpuLoadRateWith20MemoryLoadRateWhenLoadThenLoadTheSystem() throws Exception {
        final var cpuLoadRate = 50;
        final var memoryLoadRate = 20;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")
                .param(CPU_LOAD_REQUEST_PARAM_NAME, String.valueOf(cpuLoadRate))
                .param(MEMORY_LOAD_REQUEST_PARAM_NAME, String.valueOf(memoryLoadRate))
                .accept(MediaType.APPLICATION_JSON).characterEncoding(UTF_8.displayName())
        ).andExpect(status().isOk()).andReturn();

        verify(cpuLoadService, times(1)).load(eq(cpuLoadRate));
        verify(memoryLoadService, times(1)).load(eq(memoryLoadRate));
        Assertions.assertEquals(SUCCESS_MESSAGE, mvcResult.getResponse().getContentAsString());
    }

}
