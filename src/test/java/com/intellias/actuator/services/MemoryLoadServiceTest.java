package com.intellias.actuator.services;

import com.intellias.actuator.services.impl.MemoryLoadServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemoryLoadServiceTest {
    private final MemoryLoadServiceImpl memoryLoadService = new MemoryLoadServiceImpl();

    @Test
    void testWith50MemoryLoadRate() {
        final var loadRate = 50;
        final var numbers = memoryLoadService.load(loadRate);
        Assertions.assertEquals(loadRate, numbers.size());
    }

    @Test
    void testWithZeroMemoryLoadRate() {
        Assertions.assertTrue(memoryLoadService.load(0).isEmpty());
    }
}
