package com.example.kafka_challenge_3a.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "currencies")
public class Currencies {

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    Date date;
    @ElementCollection
    @CollectionTable(name = "exchange_rates")
    @MapKeyColumn(name = "currency")
    @Column(name = "rates")
    private Map<String,Double> rates = new HashMap<>(); ;



}
