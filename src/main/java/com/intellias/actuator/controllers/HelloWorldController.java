package com.intellias.actuator.controllers;

import com.intellias.actuator.services.AuditService;
import io.micrometer.core.annotation.Timed;
import java.lang.ref.Reference;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
public class HelloWorldController {
    private final AuditService auditService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Random random = new SecureRandom();

    @GetMapping("hello-world")
    @SneakyThrows
    public String helloWorld(
            @RequestParam(name = "cpu-load-rate", defaultValue = "0") Integer cpuLoadRate,
            @RequestParam(name = "memory-load-rate", defaultValue = "0") Integer memoryLoadRate
    ) {
        auditService.logHelloWorldRequest();
        if (cpuLoadRate > 0) {
            final var collect = IntStream.range(0, cpuLoadRate)
                    .mapToObj(i -> (Callable<String>) () -> shaAlgorithm(UUID.randomUUID().toString()))
                    .collect(Collectors.toList());
            final var futures = executorService.invokeAll(collect, 5, TimeUnit.SECONDS);
            log.info("CPU sensitive task executed: {} of {}", futures.stream().filter(Future::isDone).count(),
                    futures.size());
        }

        if (memoryLoadRate > 0) {
            final var numbers = new HashSet<BigInteger>();
            IntStream.range(0, cpuLoadRate).parallel().forEach(
                    i -> numbers.add(BigInteger.probablePrime(4096, random))
            );
            log.info("Memory consumption executed {} data", numbers.size());
        }
        log.info("Hello World request received, CPU Load Rate: {} / Memory Load Rate: {}", cpuLoadRate, memoryLoadRate);
        return "{\"message\": \"success\"}";
    }

    @SneakyThrows
    public String shaAlgorithm(String hash) {
        String password = "Secret";
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return new BigInteger(digest).toString(16);
    }
}
