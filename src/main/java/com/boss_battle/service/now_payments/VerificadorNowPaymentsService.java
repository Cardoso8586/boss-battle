package com.boss_battle.service.now_payments;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.model.DepositoBossCoins;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.DepositoBossCoinsRepository;
import com.boss_battle.repository.UsuarioBossBattleRepository;

@Service
public class VerificadorNowPaymentsService {

    @Autowired
    private DepositoBossCoinsRepository depositoRepository;

    @Autowired
    private NowPaymentsService nowPaymentsService;

    @Autowired
    private UsuarioBossBattleRepository usuarioRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void verificarPendentes() {

        List<DepositoBossCoins> pendentes =
                depositoRepository.findByCreditadoFalse();

        for (DepositoBossCoins deposito : pendentes) {

            try {

                Map pagamento =
                        nowPaymentsService.consultarPagamento(
                                deposito.getPaymentId()
                        );

                String status =
                        String.valueOf(
                                pagamento.get("payment_status")
                        );

                System.out.println("VERIFICANDO: "
                        + deposito.getPaymentId()
                        + " STATUS: "
                        + status);

                if (
                        "finished".equalsIgnoreCase(status)
                        || "partially_paid".equalsIgnoreCase(status)
                        || "sending".equalsIgnoreCase(status)
                ) {

                    UsuarioBossBattle usuario =
                            usuarioRepository
                                    .findById(deposito.getUsuarioId())
                                    .orElseThrow();

                    BigDecimal saldoAtual =
                            usuario.getBossCoins() == null
                                    ? BigDecimal.ZERO
                                    : usuario.getBossCoins();

                    BigDecimal valorUsd =
                            deposito.getValorUsd();

                    BigDecimal bossCoins =
                            valorUsd.multiply(
                                    BigDecimal.valueOf(10_000_000)
                            );

                    usuario.setBossCoins(
                            saldoAtual.add(bossCoins)
                    );

                    deposito.setCreditado(true);
                    deposito.setStatus(status);

                    usuarioRepository.save(usuario);
                    depositoRepository.save(deposito);

                    System.out.println("DEPÓSITO CREDITADO VIA FALLBACK");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}