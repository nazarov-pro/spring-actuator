package com.intellias.actuator.services.impl;

import com.intellias.actuator.services.AuditService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements ApplicationEventPublisherAware, AuditService {
    private ApplicationEventPublisher eventPublisher;
    private final MeterRegistry meterRegistry;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }

    @Override
    public void logHelloWorldRequest() {
        final var start = System.currentTimeMillis();
        eventPublisher.publishEvent(new ApplicationEvent("text") {
            @Override
            public Object getSource() {
                return source;
            }
        });
        meterRegistry.gauge("log.hello", System.currentTimeMillis() - start);
    }
}
