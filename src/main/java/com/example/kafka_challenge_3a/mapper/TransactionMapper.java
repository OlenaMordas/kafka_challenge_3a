package com.example.kafka_challenge_3a.mapper;

import com.example.kafka_challenge_3a.entity.TransactionEntity;
import com.example.kafka_challenge_3a.models.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    @Mappings({
            @Mapping(target = "transactionId", source = "id"),
            @Mapping(target = "accountId", source = "accountId"),
            @Mapping(target = "amount", source = "amount"),
            @Mapping(target = "iban", source = "iban"),
            @Mapping(target = "valueDate", source = "valueDate"),
            @Mapping(target = "description", source = "description"),
    })
    TransactionEntity mapTo(Transaction transaction);

    @InheritInverseConfiguration
    Transaction mapTo(TransactionEntity transactionEntity);
}
