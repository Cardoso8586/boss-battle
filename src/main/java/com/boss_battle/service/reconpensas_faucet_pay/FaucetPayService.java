package com.boss_battle.service.reconpensas_faucet_pay;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boss_battle.enums.FaucetPayStatus;
import com.boss_battle.model.BossBattleTransactionHistory;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.BossBattleTransactionHistoryRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FaucetPayService {

    @Value("${faucetpay.api.key}")
    private String apiKey;

    private static final BigDecimal BOSS_POR_USDT = new BigDecimal("10000000");
    private static final BigDecimal BOSS_MINIMO = new BigDecimal("20000");
    private static final int DAILY_WITHDRAW_LIMIT = 3;
    private final UsuarioBossBattleRepository usuarioRepo;
    private final CoinPriceService coinPriceService;
    private final BossBattleTransactionHistoryRepository historyRepository;

    public FaucetPayService(
    		BossBattleTransactionHistoryRepository historyRepository, 
    		UsuarioBossBattleRepository usuarioRepo,
    		CoinPriceService coinPriceService
    		) 
     {
        this.historyRepository = historyRepository;
        this.usuarioRepo = usuarioRepo;
        this.coinPriceService =coinPriceService;
    }

    /**
     * Chama API FaucetPay
     */
    private String callApi(String url, MultipartEntityBuilder builder)
            throws IOException, ParseException {

        HttpPost post = new HttpPost(url);
        post.setEntity(builder.build());

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return EntityUtils.toString(client.execute(post).getEntity());
        }
    }
    

    //====================== RESET AUTOMÁTICO ======================

   
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void resetarSaquesDiariosAutomaticamente() {
        executarResetSeNecessario();
    }

    @PostConstruct
    @Transactional
    public void resetarAoSubirAplicacao() {
        executarResetSeNecessario();
    }
    @Transactional
    public void executarResetSeNecessario() {
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        int atualizados = usuarioRepo.resetarSaquesDiarios(hoje);

        System.out.println("Reset diário de saques aplicado. Usuários atualizados: " + atualizados);
    }
    /*
    @Transactional
    public void executarResetSeNecessario() {
        List<UsuarioBossBattle> usuarios = usuarioRepo.findAll();
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        for (UsuarioBossBattle usuario : usuarios) {
            LocalDate ultimaData = usuario.getDataControleSaque();

            if (ultimaData == null) {
                usuario.setQuantidadeSaquesDiario(0);
                usuario.setDataControleSaque(hoje);
                continue;
            }

            while (ultimaData.isBefore(hoje)) {
                usuario.setQuantidadeSaquesDiario(0);
                ultimaData = ultimaData.plusDays(1);
            }

            usuario.setDataControleSaque(hoje);
        }

        usuarioRepo.saveAll(usuarios);
    }
    
    /*
/*
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    @Transactional
    public void resetarSaquesDiariosAutomaticamente() {
    List<UsuarioBossBattle> usuarios = usuarioRepo.findAll();

    LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
    System.out.println("🔥 EXECUTOU AGORA: " + LocalDateTime.now());

    for (UsuarioBossBattle usuario : usuarios) {
        usuario.setQuantidadeSaquesDiario(0);
        usuario.setDataControleSaque(hoje);
    }

    usuarioRepo.saveAll(usuarios);
}
*/


	/**
     * Consulta saldo com cache
     */
    @Cacheable(value = "saldoCache", key = "#currency")
    public String getBalance(String currency) throws IOException, ParseException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addTextBody("api_key", apiKey)
                .addTextBody("currency", currency);

        String response = callApi("https://faucetpay.io/api/v1/get_balance", builder);

        if (!response.contains("\"status\":200")) {
            throw new RuntimeException("Erro ao consultar saldo: " + response);
        }

        return response;
    }

    /**
     * Envia fundos para FaucetPay usando E-MAIL (modo faucet real)
     */
    @CacheEvict(value = "saldoCache", key = "#currency")
    public String sendFunds(
            Long userId,
            String currency,
            BigDecimal bossCoinParaSaque,
            String email,
            String note
    ) throws IOException, ParseException {

        UsuarioBossBattle usuario = usuarioRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BigDecimal saldoAtual = usuario.getBossCoins();

        // 🔐 Validações
     
        // 🔐 Normaliza quantidade de saques
        int quantidadeSaquesHoje = usuario.getQuantidadeSaquesDiario() == null 
                ? 0 
                : usuario.getQuantidadeSaquesDiario();
        // 🔐 Validações

        if(quantidadeSaquesHoje >= DAILY_WITHDRAW_LIMIT){
            throw new RuntimeException("Limite diário de saques atingido");
        }

        if (bossCoinParaSaque.compareTo(BOSS_MINIMO) < 0) {
            throw new RuntimeException("Saque mínimo: 100.000 BossCoin");
        }

        if (saldoAtual.compareTo(bossCoinParaSaque) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
        /*
        if(usuario.getQuantidadeSaquesDiario() >= DAILY_WITHDRAW_LIMIT){
            throw new RuntimeException("Limite diário de saques atingido");
        }
        
        if (bossCoinParaSaque.compareTo(BOSS_MINIMO) < 0) {
            throw new RuntimeException("Saque mínimo: 100.000 BossCoin");
        }

        if (saldoAtual.compareTo(bossCoinParaSaque) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
*/
        // 🔁 Conversões reais
        BigDecimal usdt = bossParaUsdt(bossCoinParaSaque);
        BigDecimal valorMoeda = usdtParaMoeda(usdt, currency);

        // FaucetPay units
        BigDecimal multiplier = getMultiplier(currency);
        String amountConverted = valorMoeda
                .multiply(multiplier)
                .setScale(0, RoundingMode.DOWN)
                .toPlainString();
        //debug
        /*
        System.out.println("Boss: " + bossCoinParaSaque);
        System.out.println("USDT: " + usdt);
        System.out.println("Valor moeda: " + valorMoeda);
        System.out.println("Amount FaucetPay: " + amountConverted);
        */
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                .addTextBody("api_key", apiKey)
                .addTextBody("currency", currency)
                .addTextBody("amount", amountConverted)
                .addTextBody("to", email)
                .addTextBody("username", email);

        if (note != null && !note.isBlank()) {
            builder.addTextBody("note", note);
        }

        String response = callApi("https://faucetpay.io/api/v1/send", builder);

       

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        int status = json.get("status").asInt();

        FaucetPayStatus statusTx = switch (status) {
            case 200 -> FaucetPayStatus.APROVADO;
            case 402 -> FaucetPayStatus.REJEITADO_ADMINISTRADOR;
            case 401 -> FaucetPayStatus.NAO_AUTORIZADO;
            default -> FaucetPayStatus.ERRO_DESCONHECIDO;
        };

        // 📜 Histórico (sempre grava)
        BossBattleTransactionHistory tx = new BossBattleTransactionHistory();
        tx.setUserId(userId);
        tx.setCurrency(currency);
        tx.setAmount(valorMoeda.toPlainString());
        tx.setEmail(email);
        tx.setNote(note);
        tx.setStatus(statusTx.getMensagemUsuario());

        historyRepository.save(tx);

        // 💰 Desconto FINAL (somente se aprovado)
        if (statusTx == FaucetPayStatus.APROVADO) {
            usuario.setBossCoins(saldoAtual.subtract(bossCoinParaSaque));
            
            usuario.setQuantidadeSaquesDiario(quantidadeSaquesHoje + 1);
            usuarioRepo.save(usuario);
        }

   

        return response;
    }

    

    // Atualização automática cache
    @Scheduled(fixedRate = 300000)
    public void refreshCache() {}

    /**
     * Atualiza saldo do usuário
     */
   
   
    private BigDecimal bossParaUsdt(BigDecimal boss) {
        return boss.divide(BOSS_POR_USDT, 8, RoundingMode.DOWN);
    }

    private BigDecimal usdtParaMoeda(BigDecimal usdt, String currency) {
        if ("USDT".equalsIgnoreCase(currency)) {
            return usdt;
        }

        BigDecimal precoUsd = coinPriceService.getPrecoEmUsd(currency);

        if (precoUsd.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço inválido para " + currency);
        }

        return usdt.divide(precoUsd, 8, RoundingMode.DOWN);
    }


    private BigDecimal getMultiplier(String currency) {
        switch (currency.toUpperCase()) {
            case "BTC":
            case "LTC":
            case "DOGE":
            case "DASH":
            case "DGB":
            case "BCH":
            case "BNB":
            case "TRX":	
            	
                return new BigDecimal("100000000");

           
            case "USDT":
                return new BigDecimal("100000000");

            default:
                throw new RuntimeException("Moeda não suportada: " + currency);
        }
    }

}



