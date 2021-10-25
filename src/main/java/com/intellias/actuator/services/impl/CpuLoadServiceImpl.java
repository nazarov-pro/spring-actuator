package com.intellias.actuator.services.impl;

import static com.intellias.actuator.utils.Constants.MD5_ALGORITHM;

import com.intellias.actuator.services.CpuLoadService;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CpuLoadServiceImpl implements CpuLoadService {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    @SneakyThrows
    public void load(int rate) {
        if (rate > 0) {
            log.info("CPU load method called with {} rate.", rate);
            final var collect = IntStream.range(0, rate)
                    .mapToObj(i -> (Callable<String>) () -> shaAlgorithm(UUID.randomUUID().toString()))
                    .collect(Collectors.toList());
            final var futures = executorService.invokeAll(collect, 5, TimeUnit.SECONDS);
            log.info("CPU sensitive task executed: {} of {}", futures.stream().filter(Future::isDone).count(),
                    futures.size());
        }
    }

    @SneakyThrows
    public String shaAlgorithm(final String hash) {
        MessageDigest md = MessageDigest.getInstance(MD5_ALGORITHM);
        md.update(hash.getBytes());
        byte[] digest = md.digest();
        return new BigInteger(digest).toString(16);
    }
}
