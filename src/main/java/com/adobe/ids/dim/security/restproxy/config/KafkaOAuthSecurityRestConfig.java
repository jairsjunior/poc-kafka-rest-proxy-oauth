package com.adobe.ids.dim.security.restproxy.config;

import io.confluent.kafkarest.KafkaRestConfig;
import io.confluent.rest.RestConfigException;
import io.confluent.kafkarest.SystemTime;
import org.apache.kafka.common.config.ConfigDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;

public final class KafkaOAuthSecurityRestConfig extends KafkaRestConfig {

    private static final ConfigDef configDef = createBaseConfigDef();

    private final Optional<String> jwtToken;
    private final String clientId = "";
    private final String clientSecret = "";
    private final String clientCode = "";


    public KafkaOAuthSecurityRestConfig(final Properties props, final Optional<String> jwtToken) throws RestConfigException {
        super(KafkaOAuthSecurityRestConfig.configDef, props, new SystemTime());
        this.jwtToken = jwtToken;
    }

    public Properties getProducerProperties() {
        final Properties originalProps = super.getProducerProperties();
        Properties secureProps = new Properties();
        if (this.jwtToken.isPresent()) {
            secureProps = this.getTokenClientProps();
        }
        originalProps.putAll(secureProps);
        return originalProps;
    }

    public Properties getTokenClientProps(){
        final Properties properties = new Properties();
        properties.put("sasl.mechanism", "OAUTHBEARER");
        properties.put("sasl.login.callback.handler.class", "com.adobe.ids.dim.security.IMSAuthenticateLoginCallbackHandler");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required  " +
                "ims.token.url=\"" + this.getString("ims.token.url")  + "\"" +
                "ims.grant.type=\"authorization_code\" " +
                "ims.client.id=\"" + clientId + "\"" +
                "ims.client.secret=\"" + clientSecret + "\"" +
                "ims.client.code=\"" + clientCode + "\";");
        return properties;
    }


    //TODO: Other stuff to see later.. needs organization
    private static ConfigDef createBaseConfigDef() {
        return baseKafkaRestConfigDef().
                define(
                        "confluent.rest.auth.propagate.method",
                        ConfigDef.Type.STRING,
                        (Object)"",
                        (ConfigDef.Validator)ConfigDef.ValidString.in(getMountValidNames()),
                        ConfigDef.Importance.LOW,
                        "The mechanism used to authenticate Rest Proxy requests. When broker security is enabled, the principal from this authentication mechanism is propagated to Kafka broker requests.")
                .define("confluent.license",
                        ConfigDef.Type.STRING,
                        (Object)"",
                        ConfigDef.Importance.HIGH,
                        "Confluent will issue a license key to each subscriber. The license key will be a short snippet of text that you can copy and paste. Without the license key, you can use Confluent Security Plugins for a 30-day trial period. If you are a subscriber and don't have a license key, please contact Confluent Support at support@confluent.io.")
                .define(
                        "confluent.metadata.bootstrap.server.urls",
                        ConfigDef.Type.STRING,
                        (Object)"",
                        ConfigDef.Importance.HIGH,
                        "Comma separated list of bootstrap metadata servers urls to which this Rest proxy connects to. For ex: http://localhost:8080,http://localhost:8081")
                .define(
                        "confluent.rest.auth.ssl.principal.mapping.rules",
                        ConfigDef.Type.LIST,
                        (Object) Collections.singletonList("DEFAULT"),
                        ConfigDef.Importance.LOW,
                        "A list of rules to map from the distinguished name (DN) in the client certificate to a short name principal for authentication with the Kafka broker. Rules are tested from left to right. The first rule that matches will be applied."
                );
    }


    public static final String[] getMountValidNames() {
        ArrayList<String> validNames = new ArrayList<String>();
        validNames.add("OAuth");
        validNames.add("");
        return (String[])validNames.toArray();
    }
}
