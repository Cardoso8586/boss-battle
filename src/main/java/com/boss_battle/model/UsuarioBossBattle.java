package com.boss_battle.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_boss_battle")
public class UsuarioBossBattle  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Column(name = "password_hash")
    private String senha;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    
    
    //================================ boss_coins =======================================
    @Column(name = "boss_coins", precision = 19, scale = 2)
    private BigDecimal bossCoins = BigDecimal.ZERO;
   //
    public BigDecimal getBossCoins() { return bossCoins; }
    public void setBossCoins(BigDecimal bossCoins) { this.bossCoins = bossCoins; }
   //================================ boss_coins =======================================




    // ===== ATRIBUTOS DO SISTEMA DE BATALHA =====
    @Column(name = "nivel")
    private int nivel = 1;

    @Column(name = "exp")
    private long exp = 0;

    //========================== ATAQUE BASE =====================================
    
    @Column(name = "ataque_base", nullable = false)
    private long ataqueBase = 10;
    
    public void setAtaqueBase(long ataqueBase) { this.ataqueBase = ataqueBase; }
    
    
    public long getAtaqueBase() {
        if (ataqueBase <= 0 ) {
           ataqueBase = 10L;
           setAtaqueBase(10L);
        }
        return ataqueBase;
    }
    
    
    
    
 //========================== FIM ATAQUE BASE =====================================

 //======================  quantidade em estoque ====================================
    @Column(name = "pocao_vigor", nullable = false)
    private long pocaoVigor; // estoque total

    @Column(name = "pocao_vigor_ativa", nullable = false)
    private long pocaoVigorAtiva; // quantidade equipada


    
 // getters / setters
    public long getPocaoVigor() {
        return pocaoVigor;
    }

    public void setPocaoVigor(long pocaoVigor) {
        this.pocaoVigor = pocaoVigor;
    }

    public long getPocaoVigorAtiva() {
        return pocaoVigorAtiva;
    }

    public void setPocaoVigorAtiva(long pocaoVigorAtiva) {
        this.pocaoVigorAtiva = pocaoVigorAtiva;
    }

    //----------------------------------------------------------------------------------
    
    
    
    // ⏱️ último ataque ao boss
    @Column(name = "ultimo_ataque_boss", columnDefinition = "DATETIME")
    private LocalDateTime ultimoAtaqueBoss;

    
  //coluna: quantidade de guerreiros
    @Column(name = "guerreiros_inventario", nullable = false)
    private Long guerreirosInventario = 1L;
    
    //==============================================================
    @Column(name = "guerreiros_retaguarda", nullable = false)
    private Long guerreirosRetaguarda = 1L;
    
    @Column(name = "recuperacao_retaguarda", nullable = false)
    private Long recuperacaoRetaguarda = 3L;
    
    public Long getGuerreirosRetaguarda() {
        return guerreirosRetaguarda;
    }

    public void setGuerreirosRetaguarda(Long guerreirosRetaguarda) {
        this.guerreirosRetaguarda = guerreirosRetaguarda;
    }

    public Long getRecuperacaoRetaguarda() {
        return recuperacaoRetaguarda;
    }

    public void setRecuperacaoRetaguarda(Long recuperacaoRetaguarda) {
        this.recuperacaoRetaguarda = recuperacaoRetaguarda;
    }
//=====================================================================
    
    //coluna: quantidade de guerreiros
    @Column(name = "guerreiros", nullable = false)
    private Long guerreiros = 1L;

    
    @Column(name = "ataque_base_guerreiros", nullable = false)
    private Long ataqueBaseGuerreiros = 5L;

  
    @Column(name = "energia_guerreiros", nullable = false)
    private Long energiaGuerreiros = 1000L;


    @Column(name = "energia_guerreiros_padrao", nullable = false)
    private Long energiaGuerreirosPadrao = 1000L;
    
    
    // =======================  REFERIDOS ===============================
    //Ganhos pendentes (acumulando até o usuário "claimar")
    @Column(name = "referred_by", nullable = true)
    private Long referredBy;

    //@Column(name = "ganhos_pendentes", precision = 18, scale = 8)
   // private BigDecimal ganhosPendentes = BigDecimal.ZERO;
    
    
    @Column(name = "ganhos_pendentes_referral", precision = 18, scale = 8)
    private BigDecimal ganhosPendentesReferral = BigDecimal.ZERO;
  
/*
	public BigDecimal getGanhosPendentes() {
	    return ganhosPendentes;
	}

	public void setGanhosPendentes(BigDecimal ganhosPendentes) {
	    this.ganhosPendentes = ganhosPendentes;
	}
*/

	public BigDecimal getGanhosPendentesReferral() {
	    return ganhosPendentesReferral;
	}

	public void setGanhosPendentesReferral(BigDecimal ganhosPendentesReferral) {
	    this.ganhosPendentesReferral = ganhosPendentesReferral;
	}

    
    public Long getReferredBy() { return referredBy; }
    public void setReferredBy(Long referredBy) { this.referredBy = referredBy; }
    //===========================================
    
    
    
    //===========================================
    //Loja  INICO
    //===========================================
    
    @Column(name = "preco_guerreiros", nullable = false)
    private Long precoGuerreiros = 1000L;

    @Column(name = "preco_energia", nullable = false)
    private Long precoEnergia = 2000L;

  

    @Column(name = "preco_ataque_especial", nullable = false)
    private Long precoAtaqueEspecial = 3000L;
    
  //===========================================
 // GETTERS E SETTERS - LOJA
 //===========================================
    
    
 public Long getPrecoGuerreiros() {
     return precoGuerreiros;
 }

 public void setPrecoGuerreiros(Long precoGuerreiros) {
     this.precoGuerreiros = precoGuerreiros;
 }

 public Long getPrecoEnergia() {
     return precoEnergia;
 }

 public void setPrecoEnergia(Long precoEnergia) {
     this.precoEnergia = precoEnergia;
 }



 public Long getPrecoAtaqueEspecial() {
     return precoAtaqueEspecial;
 }

 public void setPrecoAtaqueEspecial(Long precoAtaqueEspecial) {
     this.precoAtaqueEspecial = precoAtaqueEspecial;
 }

 //===========================================
 // FIM GETTERS E SETTERS - LOJA
 //===========================================

   //---> FIM LOJA
    
    // ==========================================
    // CONSTRUTORES
    // ==========================================

   
    
    public UsuarioBossBattle() {
        this.bossCoins = BigDecimal.ZERO;
    }

    public UsuarioBossBattle(String username, String email, BigDecimal saldoInicial) {
        this.username = username;
        this.email = email;
        this.bossCoins = saldoInicial;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.bossCoins == null) this.bossCoins = BigDecimal.ZERO;
    }


    // ==========================================
    // SISTEMA DE ATAQUE E EXP
    // ==========================================


    public void addExp(long amount) {
        this.exp += amount;

        // Checa se sobe de nível
        long expNecessario = nivel * 100;

        while (this.exp >= expNecessario) {
            this.exp -= expNecessario;
            this.nivel++;
            this.ataqueBase += 5;

            expNecessario = nivel * 100;
        }
    }

    public void addBossCoins(long amount) {
        this.bossCoins = this.bossCoins.add(BigDecimal.valueOf(amount));
    }

    // ==========================================
    // GETTERS E SETTERS
    // ==========================================
    
    public Long getEnergiaGuerreirosPadrao() {
        return energiaGuerreirosPadrao;
    }

    public void setEnergiaGuerreirosPadrao(Long energiaGuerreirosPadrao) {
        this.energiaGuerreirosPadrao = energiaGuerreirosPadrao;
        
       
    }
    
    public Long getEnergiaGuerreiros() {
        return energiaGuerreiros;
    }

    public void setEnergiaGuerreiros(Long energiaGuerreiros) {
        this.energiaGuerreiros = energiaGuerreiros;
        
       
    }

    
    public long getAtaqueBaseGuerreiros() {
        return ataqueBaseGuerreiros != null ? ataqueBaseGuerreiros : 1L;
    }

    public void setAtaqueBaseGuerreiros(Long ataqueBaseGuerreiros) {
        this.ataqueBaseGuerreiros = ataqueBaseGuerreiros;
    }

    
    public long getGuerreirosInventario() {
        return guerreirosInventario != null ? guerreirosInventario : 1L;
    }


    public void setGuerreirosInventario(long guerreirosInventario) {
        this.guerreirosInventario = guerreirosInventario;
    }
    
    
    
    public long getGuerreiros() {
        return guerreiros != null ? guerreiros : 1L;
    }


    public void setGuerreiros(long guerreiros) {
        this.guerreiros = guerreiros;
    }
    
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getCreatedAt() { return createdAt; }


    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public long getExp() { return exp; }
    public void setExp(long exp) { this.exp = exp; }


   

	public LocalDateTime getUltimoAtaqueBoss() {
		return ultimoAtaqueBoss;
	}

	public void setUltimoAtaqueBoss(LocalDateTime ultimoAtaqueBoss) {
		this.ultimoAtaqueBoss = ultimoAtaqueBoss;
	}




}
