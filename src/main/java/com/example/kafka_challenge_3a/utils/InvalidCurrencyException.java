package com.example.kafka_challenge_3a.utils;

import lombok.Getter;

public class InvalidCurrencyException extends RuntimeException {

    @Getter
    String message;
    public InvalidCurrencyException(String message) { this.message = message; }

}
