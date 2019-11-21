package com.adobe.ids.dim.security.restproxy.context;

import com.adobe.ids.dim.security.restproxy.config.KafkaOAuthSecurityRestConfig;
import io.confluent.kafkarest.*;
import io.confluent.kafkarest.extension.KafkaRestContextProvider;
import io.confluent.kafkarest.v2.KafkaConsumerManager;

import java.security.Principal;
import java.util.Map;
import java.util.HashMap;

public class KafkaOAuthRestContextFactory {

    private static final KafkaOAuthRestContextFactory instance = new KafkaOAuthRestContextFactory();
    private final Map<String, KafkaRestContext> userToContextMap;

    private KafkaOAuthRestContextFactory() {
        this.userToContextMap = new HashMap<String, KafkaRestContext>();
    }

    public static KafkaOAuthRestContextFactory getInstance() {
        return KafkaOAuthRestContextFactory.instance;
    }

    public KafkaRestContext getContext(final Principal principal, final KafkaOAuthSecurityRestConfig kafkaRestConfig, final String resourceType, final boolean tokenAuth) {
        if (this.userToContextMap.containsKey(principal.getName())) {
            return this.userToContextMap.get(principal.getName());
        }
        synchronized (principal.getName().intern()) {
            final ScalaConsumersContext scalaConsumersContext = KafkaRestContextProvider.getDefaultContext().getScalaConsumersContext();
            final KafkaRestContext context = new DefaultKafkaRestContext(kafkaRestConfig, null, null, null, scalaConsumersContext);
            this.userToContextMap.put(principal.getName(), context);
        }
        return this.userToContextMap.get(principal.getName());
    }

    public void clean() {
        for (final KafkaRestContext context : this.userToContextMap.values()) {
            context.shutdown();
        }
        this.userToContextMap.clear();
    }
}
