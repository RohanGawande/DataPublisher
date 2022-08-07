package com.product.data.publisher.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BalanceDetails implements Serializable {

    private String balanceId;
    private String accountId;
    private Float balance;
}
