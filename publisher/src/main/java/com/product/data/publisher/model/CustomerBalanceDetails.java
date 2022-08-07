package com.product.data.publisher.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerBalanceDetails implements Serializable {

    private String customerId;
    private String phoneNumber;
    private String accountId;
    private Float balance;
}
