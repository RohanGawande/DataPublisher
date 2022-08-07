package com.product.data.publisher.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerDetails implements Serializable {

    private String customerId;
    private String name;
    private String phoneNumber;
    private String accountId;
}
