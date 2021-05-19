package com.example.kafka_challenge_3a.utils;

import lombok.Getter;

public class InvalidAccountNumberException extends RuntimeException {

    @Getter
    String message;
    public InvalidAccountNumberException(String message) { this.message = message; }

}
