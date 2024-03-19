package com.wsq.wsqapiclientsdk;

import com.wsq.wsqapiclientsdk.client.WsqApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wsq
 * @Date: 2024/3/4 10:17
 */
@Data
@Configuration
@ConfigurationProperties("wsqapi.client")
@ComponentScan
public class WsqApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public WsqApiClient wsqApiClient(){
        return new WsqApiClient(accessKey, secretKey);
    }
}
