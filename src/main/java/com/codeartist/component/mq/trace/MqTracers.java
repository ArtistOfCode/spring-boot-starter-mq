package com.codeartist.component.mq.trace;

import com.codeartist.component.core.support.mq.bean.MqContext;
import com.codeartist.component.core.support.mq.bean.MqMessage;
import com.codeartist.component.core.support.trace.Tracers;

import java.util.Collections;
import java.util.Map;

/**
 * MQ链路
 *
 * @author J.N.AI
 * @date 2023/8/1
 */
public class MqTracers {

    private static final String TOPIC = "Topic";

    private final Tracers tracers;

    public MqTracers(Tracers tracers) {
        this.tracers = tracers;
    }

    public void producer(MqMessage message, Runnable runnable) {
        Map<String, String> tags = Collections.singletonMap(TOPIC, message.getTopic());
        tracers.startScopedSpan(message.getType().name() + " mq producer", tags, () -> {
            runnable.run();
            return null;
        }, message.getHeaders());
    }

    public void consumer(MqContext context, Runnable runnable) {
        Map<String, String> tags = Collections.singletonMap(TOPIC, context.getTopic());
        tracers.startSpan(context.getType().name() + " mq consumer", tags, () -> {
            runnable.run();
            return null;
        }, context.getHeaders());
    }
}
