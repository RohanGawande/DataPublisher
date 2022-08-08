package com.product.data.publisher.configuration;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@Configuration
public class ApplicationConfig {

    //Kafka Config
    private String ApplicationId;
    private String bootstrapServers;

    //Topic Config
    private String customerDetailsTopic;
    private String balanceDetailsTopic;
    private String outputTopic;
}
