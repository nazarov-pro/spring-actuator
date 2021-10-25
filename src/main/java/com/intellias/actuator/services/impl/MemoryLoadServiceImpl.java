package com.intellias.actuator.services.impl;

import static com.intellias.actuator.utils.Constants.PRIME_BIT_LENGTH;

import com.intellias.actuator.services.MemoryLoadService;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemoryLoadServiceImpl implements MemoryLoadService {
    private final Random random = new Random();

    @Override
    public Set<BigInteger> load(int rate) {
        if (rate > 0) {
            log.info("Memory load method called with {} rate.", rate);
            final var numbers = new HashSet<BigInteger>();
            IntStream.range(0, rate).parallel().forEach(i -> numbers.add(generate()));
            log.info("Memory consumption executed {} data", numbers.size());
            return numbers;
        }
        return Collections.emptySet();
    }

    public BigInteger generate() {
        byte[] bytes = new byte[4096];
        random.nextBytes(bytes);
        bytes[0] |= 0x80;
        return new BigInteger(1, bytes);
    }
}
