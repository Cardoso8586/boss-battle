package com.boss_battle.service.missoes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boss_battle.dto.UsuarioRankingAtaquesDTO;
import com.boss_battle.model.UsuarioBossBattle;
import com.boss_battle.repository.UsuarioBossBattleRepository;
import com.boss_battle.service.ReferidosRecompensaService;
import com.boss_battle.service.UltimoValorRecebidoService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Service
@Transactional
public class RankingAtaqueEspecialService {

    private final UsuarioBossBattleRepository usuarioRepo;
    private final ReferidosRecompensaService referidosService;
    private final UltimoValorRecebidoService ultimoValorRecebidoService;

    public RankingAtaqueEspecialService(
    		UsuarioBossBattleRepository usuarioRepo,
    		ReferidosRecompensaService referidosService,
    		UltimoValorRecebidoService ultimoValorRecebidoService
    		
    		) {
        this.usuarioRepo = usuarioRepo;
        this.referidosService = referidosService;
        this.ultimoValorRecebidoService = ultimoValorRecebidoService;
    }

    // Distribui prêmios semanais para os 5 melhores usuários
    public List<PremioUsuario> distribuirPremios() {
        BigDecimal premioPrimeiroLugar = BigDecimal.valueOf(50000);
        BigDecimal premioSegundoLugar  = BigDecimal.valueOf(30000);
        BigDecimal premioTerceiroLugar = BigDecimal.valueOf(20000);
        BigDecimal premioQuartoLugar   = BigDecimal.valueOf(10000);
        BigDecimal premioQuintoLugar   = BigDecimal.valueOf(5000);
        
 
        List<UsuarioBossBattle> top5 = usuarioRepo.findTop5ByOrderByQuantidadeAtaquesSemanalDesc();
        List<PremioUsuario> premiados = new ArrayList<>();

        for (int i = 0; i < top5.size(); i++) {
            UsuarioBossBattle u = top5.get(i);
            BigDecimal premio = BigDecimal.ZERO;

            switch (i) {
                case 0:
                    premio = premioPrimeiroLugar;
                    break;
                case 1:
                    premio = premioSegundoLugar;
                    break;
                case 2:
                    premio = premioTerceiroLugar;
                    break;
                case 3:
                    premio = premioQuartoLugar;
                    break;
                case 4:
                    premio = premioQuintoLugar;
                    break;
            }

            //u.setPremioEspecialPendente(premio);
            BigDecimal pendenteAtual = u.getPremioEspecialPendente() == null ? BigDecimal.ZERO : u.getPremioEspecialPendente();
            if (pendenteAtual.compareTo(BigDecimal.ZERO) <= 0) {
                u.setPremioEspecialPendente(premio);
                usuarioRepo.save(u);
            }
            
            
            usuarioRepo.save(u);

            premiados.add(new PremioUsuario(u.getId(), u.getUsername(), premio));
        }

        return premiados;
    }

    // Confirma o prêmio (move de pendente para bossCoins)
    @Transactional
    public boolean confirmarPremio(Long usuarioId) {
        return usuarioRepo.findById(usuarioId)
                .map(u -> {
                    BigDecimal pendente = u.getPremioEspecialPendente();
                    if (pendente.compareTo(BigDecimal.ZERO) > 0) {
                        u.setBossCoins(u.getBossCoins().add(pendente));
                        u.setPremioEspecialPendente(BigDecimal.ZERO);
                        
                        
                        referidosService.adicionarGanho(u, (pendente));
                        ultimoValorRecebidoService.setUltimoValorRecebido(u,pendente);
                        
                        
                        usuarioRepo.save(u);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
  
   // @Scheduled(cron = "*/10 * * * * *", zone = "America/Sao_Paulo")
    @Scheduled(cron = "0 0 0 * * SUN", zone = "America/Sao_Paulo")
    public void distribuirPremiosAgendado() {
        System.out.println("🔥 EXECUTOU AGORA: " + java.time.LocalDateTime.now());

        try {
            distribuirPremios();
            System.out.println("✅ distribuiu premios");

            resetarVisualizacoesSemanais();
            System.out.println("✅ resetou semanal");
        } catch (Exception e) {
            System.out.println("❌ ERRO NO AGENDADO: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Zera a contagem semanal de ataques especiais
    @Transactional
    public void resetarVisualizacoesSemanais() {
        List<UsuarioBossBattle> usuarios = usuarioRepo.findAll();
        for (UsuarioBossBattle u : usuarios) {
            u.setQuantidadeAtaquesSemanal(0);
        }
        usuarioRepo.saveAll(usuarios);
    }

    public List<UsuarioBossBattle> getRankingSemanalTop(int top) {
        Pageable pageable = PageRequest.of(0, top, Sort.by(Sort.Direction.DESC, "quantidadeAtaquesSemanal"));
        return usuarioRepo.findTopNByOrderByQuantidadeAtaquesSemanalDesc(pageable);
    }

    public UsuarioRankingAtaquesDTO getMinhaPosicao(Long usuarioId) {
        // Busca os top 100 (ou outro número alto) para não quebrar quem está muito abaixo
        List<UsuarioBossBattle> usuarios = usuarioRepo.findTopNByOrderByQuantidadeAtaquesSemanalDesc(100);

        for (int i = 0; i < usuarios.size(); i++) {
            UsuarioBossBattle u = usuarios.get(i);
            if (u.getId().equals(usuarioId)) {
                return new UsuarioRankingAtaquesDTO(
                    u.getId(),
                    u.getUsername(),
                    u.getQuantidadeAtaquesSemanal(),
                    i + 1
                );
            }
        }
        return null;
    }
    
    
    public void incrementarAtaquesSemanais(UsuarioBossBattle usuario) {
        Integer atual = usuario.getQuantidadeAtaquesSemanal();
        usuario.setQuantidadeAtaquesSemanal(Optional.ofNullable(atual).orElse(0) + 1);
        System.out.println("Quantidade ranking semanal:   " + usuario.getQuantidadeAtaquesSemanal());
    }

    // DTO para retorno do prêmio
    public static class PremioUsuario {
        private Long usuarioId;
        private String nomeUsuario;
        private BigDecimal premio;

        public PremioUsuario(Long usuarioId, String nomeUsuario, BigDecimal premio) {
            this.usuarioId = usuarioId;
            this.nomeUsuario = nomeUsuario;
            this.premio = premio;
        }

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public String getNomeUsuario() {
            return nomeUsuario;
        }

        public void setNomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
        }

        public BigDecimal getPremio() {
            return premio;
        }

        public void setPremio(BigDecimal premio) {
            this.premio = premio;
        }
    }

    // Pega o valor pendente de prêmio de um usuário
    public BigDecimal getPremioPendente(Long id) {
        return usuarioRepo.findById(id)
                .map(UsuarioBossBattle::getPremioEspecialPendente)
                .orElse(BigDecimal.ZERO);
    }

}
