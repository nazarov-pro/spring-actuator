package com.intellias.actuator.controllers;

import static com.intellias.actuator.utils.Constants.CPU_LOAD_REQUEST_PARAM_NAME;
import static com.intellias.actuator.utils.Constants.MEMORY_LOAD_REQUEST_PARAM_NAME;
import static com.intellias.actuator.utils.Constants.RESOURCE_LOAD_METRIC_NAME;
import static com.intellias.actuator.utils.Constants.SUCCESS_MESSAGE;
import static com.intellias.actuator.utils.Constants.ZERO;

import com.intellias.actuator.services.CpuLoadService;
import com.intellias.actuator.services.MemoryLoadService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class ResourceLoadController {
    private final CpuLoadService cpuLoadService;
    private final MemoryLoadService memoryLoadService;

    @GetMapping
    @SneakyThrows
    @Timed(value = RESOURCE_LOAD_METRIC_NAME, histogram = true)
    public String load(
            @RequestParam(name = CPU_LOAD_REQUEST_PARAM_NAME, defaultValue = ZERO) Integer cpuLoadRate,
            @RequestParam(name = MEMORY_LOAD_REQUEST_PARAM_NAME, defaultValue = ZERO) Integer memoryLoadRate
    ) {
        log.info("Load request received, CPU Load Rate: {} / Memory Load Rate: {}", cpuLoadRate, memoryLoadRate);
        cpuLoadService.load(cpuLoadRate);
        memoryLoadService.load(memoryLoadRate);
        return SUCCESS_MESSAGE;
    }


}
