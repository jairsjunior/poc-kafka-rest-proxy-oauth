package com.adobe.ids.dim.security.restproxy.filter;

import com.adobe.ids.dim.security.IMSBearerTokenJwt;
import com.adobe.ids.dim.security.restproxy.KafkaOAuthSecurityRestResourceExtension;
import com.adobe.ids.dim.security.restproxy.config.KafkaOAuthSecurityRestConfig;
import io.confluent.kafkarest.KafkaRestContext;
import io.confluent.kafkarest.extension.KafkaRestContextProvider;
import io.confluent.rest.RestConfigException;
import org.apache.kafka.common.security.ssl.SslPrincipalMapper;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Priority(5000)
public class OAuthFilter implements ContainerRequestFilter {

    private final KafkaOAuthSecurityRestConfig oauthSecurityRestConfig;

    public OAuthFilter(final KafkaOAuthSecurityRestConfig oauthSecurityRestConfig) {
        this.oauthSecurityRestConfig = oauthSecurityRestConfig;
    }

    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        if (containerRequestContext.getSecurityContext() != null) {
//            KafkaRestContextProvider.setCurrentContext();
        }
    }

    private KafkaRestContext getKafkaRestContext(final String resourceType, final Principal principal) throws IOException {
        KafkaRestContext context;
        if (principal instanceof IMSBearerTokenJwt) {

            KafkaOAuthSecurityRestConfig bearerTokenKafkaRestConfig = null;
            //TODO: Needs to implement the conversion of
            final String jwtToken = ((IMSBearerTokenJwt)principal).toString();
            try {
                bearerTokenKafkaRestConfig = new KafkaOAuthSecurityRestConfig(this.oauthSecurityRestConfig.getOriginalProperties(), Optional.of(jwtToken));
            }
            catch (RestConfigException e) {
                throw new IOException((Throwable)e);
            }
            context = KafkaRestContextProviderFactory.getInstance().getContext(principal, bearerTokenKafkaRestConfig, resourceType, true);
        }
        else {
            context = KafkaRestContextProviderFactory.getInstance().getContext(principal, this.oauthSecurityRestConfig, resourceType, false);
        }
        return context;
    }
}
