package com.example.kafka_challenge_3a.repository;

import com.example.kafka_challenge_3a.entity.TransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, String> {
    @Query(value = "SELECT * FROM transactions t WHERE t.account_id = ?1 AND cast(to_char(t.value_date, 'MM') as INTEGER) = ?4 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<TransactionEntity> findByClientAccountNumber(Long clientAccountNumber, int limit, int offset, int month);
}
