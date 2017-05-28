package com.uitnetwork.transactionstatistic.model;

import javax.validation.constraints.Min;

import lombok.*;

/**
 * Created by ninhdoan on 5/27/17.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Min(0)
    private double amount;

    @Min(1)
    private long timestamp;
}
