package com.product.data.publisher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.data.publisher.configuration.KafkaConnector;
import com.product.data.publisher.model.BalanceDetails;
import com.product.data.publisher.model.CustomerBalanceDetails;
import com.product.data.publisher.model.CustomerDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
@Slf4j
public class PublisherService {

    private KafkaConnector kafkaConnector;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public PublisherService(KafkaConnector kafkaConnector){
        this.kafkaConnector = kafkaConnector;
    }

    public void publishAggregateDetails(){

        KStream<String, String> kStreamCustomer = kafkaConnector.getCustomerDetailsStream();
        KStream<String, String> kStreamBalance = kafkaConnector.getBalanceDetailsStream();

        ValueJoiner<String, String, String> joiner = (customer, balance) -> {
            try {
                return merge(customer, balance);
            } catch (JsonProcessingException e) {
                log.info("Json parsing error.");
            }
            return null;
        };

        KStream<String, String> kStreamAggregate = kStreamCustomer.join(kStreamBalance, joiner, JoinWindows.of(Duration.ofSeconds(10)));
        kStreamAggregate.peek()
    }

    private String merge(String customer, String balance) throws JsonProcessingException {
        CustomerDetails customerDetails = objectMapper.readValue(customer, CustomerDetails.class);
        BalanceDetails balanceDetails = objectMapper.readValue(balance, BalanceDetails.class);

        CustomerBalanceDetails customerBalanceDetails = new CustomerBalanceDetails();
        customerBalanceDetails.setBalance(balanceDetails.getBalance());
        customerBalanceDetails.setCustomerId(customerDetails.getCustomerId());
        customerBalanceDetails.setAccountId(customerDetails.getAccountId());
        customerBalanceDetails.setPhoneNumber(customerDetails.getPhoneNumber());

        return objectMapper.writeValueAsString(customerBalanceDetails);
    }
}
