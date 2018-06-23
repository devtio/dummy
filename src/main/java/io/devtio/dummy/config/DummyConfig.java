package io.devtio.dummy.config;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class DummyConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApiClient client() throws IOException, ApiException {
        ApiClient client = Config.defaultClient();
        io.kubernetes.client.Configuration.setDefaultApiClient(client);
        return client;
    }

}
