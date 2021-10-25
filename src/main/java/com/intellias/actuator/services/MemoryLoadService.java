package com.intellias.actuator.services;

import java.math.BigInteger;
import java.util.Set;

public interface MemoryLoadService {
    Set<BigInteger> load(int rate);
}
