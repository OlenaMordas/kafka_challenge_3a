package com.example.kafka_challenge_3a.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {
    @Id
    public String transactionId;
    public Long accountId;
    public String amount;
    public String iban;
    public Date valueDate;
    public String description;

}
