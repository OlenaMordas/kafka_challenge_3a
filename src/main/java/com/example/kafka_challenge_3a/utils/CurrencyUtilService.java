package com.example.kafka_challenge_3a.utils;

import com.example.kafka_challenge_3a.entity.Currencies;
import com.example.kafka_challenge_3a.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyUtilService {

    private final CurrencyRepository currencyRepository;
    private RestTemplate restTemplate;
    private Currencies currencies;
    private final String ACCESS_KEY = "515a6f23abfda1c37aa9429de1ac18da";

    public double getExchangeRate(String currency) throws Exception {


        //check if we have already value in db
        currencies = currencyRepository.getLatestCurrencies();

        // if the rate is not in db or outdated fetch new from the api
        if(currencies==null || !isToday(currencies.getDate())){
            if (restTemplate == null) {
                RestService(new RestTemplateBuilder());
            }
            fetchCurrenciesFromApi();
        }

        switch(currency) {
            case "USD":
                return currencies.getRates().get("USD");
            case "GBP":
                return currencies.getRates().get("GBP");
            case "CHF":
                return  currencies.getRates().get("CHF");

        }
        throw new Exception(String.format("Invalid currency: %s", currency));
    }

    private void fetchCurrenciesFromApi() throws Exception {

        int attempts = 5;
        // counter for while
        int currAttempt = 0;
        // error message
        String errMessage = "";
        String url = "http://api.exchangeratesapi.io/latest?base=EUR&symbols=USD,CHF,GBP&access_key=" + ACCESS_KEY;
        Currencies tempCurrencies = null;
        while (attempts > currAttempt) {
            try {
                // use temporary object to not rewrite our old object -> if something will happen with their server we still can use old values
                tempCurrencies = this.restTemplate.getForObject(url, Currencies.class);
            } catch (Exception e) {
                errMessage = e.toString();
                ++currAttempt;
            }
            if (tempCurrencies == null && currencies == null) {
                // case both values are empty
                throw new Exception(String.format("There are no values of rates for currencies. Response: %s", errMessage));
            } else if (tempCurrencies == null && currencies != null) {
                // we will work with old values of rates
                log.warn(String.format("Please check CurrenciesService: %s. Old values of rates will be used.", errMessage));
                ++currAttempt;
            } else {
                currencies = tempCurrencies;
                currencyRepository.save(currencies);
                //to break the loop
                currAttempt = attempts;
            }

        }
    }

    private boolean isToday (Date date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String today  =dtf.format(now);
        LocalDateTime ldtDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        String currDate = dtf.format(ldtDate);
        return currDate.equals(today);
    }

    private void RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
}
