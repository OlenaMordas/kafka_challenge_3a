package com.example.kafka_challenge_3a.repository;

import com.example.kafka_challenge_3a.entity.Currencies;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currencies, String> {

    @Query(value = "SELECT * FROM currencies c ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Currencies getLatestCurrencies();
}
