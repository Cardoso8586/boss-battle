package com.boss_battle.service.now_payments;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.repository.DepositoBossCoinsRepository;

@Service
public class LimpezaDepositosService {

    @Autowired
    private DepositoBossCoinsRepository depositoRepository;

    @Transactional
    public void apagarDepositosAntigosNaoPagos() {

        LocalDateTime limite = LocalDateTime.now().minusHours(24);

        List<String> statusNaoPagos = List.of(
                "waiting",
                "confirming",
                "expired",
                "failed"
        );

        depositoRepository.deleteByStatusInAndCriadoEmBefore(
                statusNaoPagos,
                limite
        );
    }
    
    /*
     * @Scheduled(fixedRate = 60 * 60 * 1000)
@Transactional
public void apagarDepositosAntigosNaoPagos() {
    LocalDateTime limite = LocalDateTime.now().minusHours(24);

    List<String> statusNaoPagos = List.of(
            "waiting",
            "confirming",
            "expired",
            "failed"
    );

    depositoRepository.deleteByStatusInAndCriadoEmBefore(
            statusNaoPagos,
            limite
    );
}
*/
    
    
}