package com.boss_battle.service.now_payments;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.boss_battle.dto.PagamentoDTO;
import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.repository.DepositoBossCoinsRepository;

@Service
public class NowPaymentsService {

    @Autowired
    private DepositoBossCoinsRepository depositoRepository;

    @Value("${nowpayments.api-key}")
    private String apiKey;

    @Value("${nowpayments.callback-url}")
    private String callbackUrl;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.nowpayments.io/v1")
            .build();

    public PagamentoDTO criarPagamento(Long usuarioId,
                                       BigDecimal valorUsd,
                                       String moeda) {

        Map<String, Object> body = new HashMap<>();

        body.put("price_amount", valorUsd);
        body.put("price_currency", "usd");
        body.put("pay_currency", moeda.toLowerCase());

        String orderId = "USER_" + usuarioId + "_" + System.currentTimeMillis();

        body.put("order_id", orderId);
        body.put("order_description", "Deposito Boss Battle Arena");
        body.put("ipn_callback_url", callbackUrl);

        System.out.println("=================================");
        System.out.println("CRIANDO PAGAMENTO NOWPAYMENTS");
        System.out.println("CALLBACK URL: " + callbackUrl);
        System.out.println("MOEDA: " + moeda);
        System.out.println("BODY ENVIADO:");
        System.out.println(body);
        System.out.println("=================================");

        Map response = webClient.post()
                .uri("/payment")
                .header("x-api-key", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .map(errorBody -> {

                                            System.out.println("=================================");
                                            System.out.println("ERRO NOWPAYMENTS");
                                            System.out.println(errorBody);
                                            System.out.println("=================================");

                                            return new RuntimeException(errorBody);
                                        })
                )
                .bodyToMono(Map.class)
                .block();

        DepositoBossCoins deposito = new DepositoBossCoins();

        deposito.setUsuarioId(usuarioId);
        deposito.setPaymentId(String.valueOf(response.get("payment_id")));
        deposito.setOrderId(String.valueOf(response.get("order_id")));
        deposito.setMoeda(String.valueOf(response.get("pay_currency")));
        deposito.setValorUsd(valorUsd);

        deposito.setValorCripto(
                new BigDecimal(String.valueOf(response.get("pay_amount")))
        );

        deposito.setEnderecoCarteira(
                String.valueOf(response.get("pay_address"))
        );

        deposito.setStatus(
                String.valueOf(response.get("payment_status"))
        );

        deposito.setCreditado(false);

        depositoRepository.save(deposito);

        PagamentoDTO dto = new PagamentoDTO();

        dto.setPaymentId(String.valueOf(response.get("payment_id")));
        dto.setStatus(String.valueOf(response.get("payment_status")));
        dto.setMoeda(String.valueOf(response.get("pay_currency")));
        dto.setValorPagar(String.valueOf(response.get("pay_amount")));
        dto.setEnderecoCarteira(String.valueOf(response.get("pay_address")));

        dto.setQrCode(
                "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data="
                        + response.get("pay_address")
        );

        return dto;
    }
}