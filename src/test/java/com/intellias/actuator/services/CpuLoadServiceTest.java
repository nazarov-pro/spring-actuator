package com.intellias.actuator.services;

import com.intellias.actuator.services.impl.CpuLoadServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CpuLoadServiceTest {
    private final CpuLoadServiceImpl cpuLoadService = new CpuLoadServiceImpl();

    @Test
    void testWith50CpuLoadRate() {
        final var countOfThreadsStart = Thread.getAllStackTraces().keySet().size();
        cpuLoadService.load(50);
        final var countOfThreadsEnd = Thread.getAllStackTraces().keySet().size();
        Assertions.assertTrue(countOfThreadsStart < countOfThreadsEnd);
    }

    @Test
    void testWithZeroCpuLoadRate() {
        final var countOfThreadsStart = Thread.getAllStackTraces().keySet().size();
        cpuLoadService.load(0);
        final var countOfThreadsEnd = Thread.getAllStackTraces().keySet().size();
        Assertions.assertEquals(countOfThreadsStart, countOfThreadsEnd);
    }
}
