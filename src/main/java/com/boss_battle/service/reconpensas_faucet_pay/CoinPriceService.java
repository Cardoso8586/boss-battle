package com.boss_battle.service.reconpensas_faucet_pay;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoinPriceService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API =
        "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd";

    @Cacheable(value = "coinPrice", key = "#currency")
    public BigDecimal getPrecoEmUsd(String currency) {

        try {
            String id = mapCurrency(currency);
            String url = String.format(API, id);

            Map<?, ?> resp = restTemplate.getForObject(url, Map.class);

            if (resp == null || !resp.containsKey(id)) {
                return fallback(currency);
            }

            Map<?, ?> moeda = (Map<?, ?>) resp.get(id);
            Object usd = moeda.get("usd");

            return new BigDecimal(usd.toString());

        } catch (Exception e) {
            return fallback(currency);
        }
    }

    private String mapCurrency(String currency) {
        return switch (currency.toUpperCase()) {
            case "LTC"  -> "litecoin";
            case "TRX"  -> "tron";
            case "DOGE" -> "dogecoin";
            case "DGB"  -> "digibyte";
            case "USDT" -> "tether";
            default -> throw new RuntimeException("Moeda não suportada");
        };
    }

    private BigDecimal fallback(String currency) {
        return switch (currency.toUpperCase()) {
            case "LTC"  -> new BigDecimal("70");
            case "TRX"  -> new BigDecimal("0.07");
            case "DOGE" -> new BigDecimal("0.06");
            case "DGB"  -> new BigDecimal("0.007");
            case "USDT" -> BigDecimal.ONE;
            default -> BigDecimal.ZERO;
        };
    }
    
    @Scheduled(fixedRate = 300000) // 5 minutos
    @CacheEvict(value = "coinPrice", allEntries = true)
    public void limparCachePrecos() {}

}
