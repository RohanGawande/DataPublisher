package com.product.data.publisher.configuration;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Objects;
import java.util.Properties;

@Data
public class KafkaConnector {

    private final StreamsBuilder customerDetailsStreamBuilder = new StreamsBuilder();
    private final StreamsBuilder balanceDetailsStreamBuilder = new StreamsBuilder();
    private Topology customerTopology;
    private Topology balanceTopology;
    private KStream<String, String> customerDetailsStream;
    private KStream<String, String> balanceDetailsStream;
    private final ApplicationConfig applicationConfig;

    public KafkaConnector(ApplicationConfig applicationConfig){
        this.applicationConfig = applicationConfig;
    }

    synchronized KStream<String, String> getKStreamCustomerDetails(){
        if( Objects.isNull(customerDetailsStream) ){
            customerDetailsStream = customerDetailsStreamBuilder
                    .stream(getApplicationConfig().getCustomerDetailsTopic(), Consumed.with(Serdes.String(), Serdes.String()));
        }

        return customerDetailsStream;
    }

    synchronized KStream<String, String> getKStreamBalanceDetails(){
        if( Objects.isNull(balanceDetailsStream) ){
            balanceDetailsStream = balanceDetailsStreamBuilder
                    .stream(getApplicationConfig().getBalanceDetailsTopic(), Consumed.with(Serdes.String(), Serdes.String()));
        }

        return balanceDetailsStream;
    }

    public Properties getKafkaStreamProperties(Properties newProps){

        Properties configurationProperties = new Properties();

        configurationProperties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, getApplicationConfig().getApplicationId());
        configurationProperties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, getApplicationConfig().getBootstrapServers());
        configurationProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configurationProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        if(Objects.nonNull(newProps)){
            configurationProperties.putAll(newProps);
        }

        return configurationProperties;
    }
}
