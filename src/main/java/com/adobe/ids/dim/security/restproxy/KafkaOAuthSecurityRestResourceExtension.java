package com.adobe.ids.dim.security.restproxy;

import io.confluent.kafkarest.KafkaRestConfig;
import io.confluent.kafkarest.extension.RestResourceExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Configurable;

public class KafkaOAuthSecurityRestResourceExtension implements RestResourceExtension {

    private static final Logger log = LoggerFactory.getLogger(KafkaOAuthSecurityRestResourceExtension.class);

    public void register(final Configurable<?> config, final KafkaRestConfig restConfig) {
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clean(){

    }

}
