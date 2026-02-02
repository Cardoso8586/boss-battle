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

    @Column(name = "espada_flanejante", nullable = false)
    private long espadaFlanejante; // quantidade total
    
    @Column(name = "espada_flanejante_ativa", nullable = false)
    private long espadaFlanejanteAtiva; 
    
    @Column(name = "espada_flanejante_desgaste", nullable = false)
    private long espadaFlanejanteDesgaste = 100 ;
    
    
    @Column(name = "machado_dilacerador", nullable = false)
    private long machadoDilacerador; // quantidade total
    
    @Column(name = "machado_dilacerador_ativo", nullable = false)
    private long machadoDilaceradorAtivo; 
    
    @Column(name = "machado_dilacerador_desgaste", nullable = false)
    private long machadoDilaceradorDesgaste = 200 ;
    

 //===============================================================
            /// COFIGURAÇÕES DO ARCO E FLECHAS
 //===========================================================
@Column(name = "inventario_arco", nullable = false)
private long inventarioArco = 0;

@Column(name = "arco_ativo", nullable = false)
private long arcoAtivo = 0;

@Column(name = "durabilidade_arco", nullable = false)
private long durabilidadeArco = 100;

@Column(name = "flecha_fogo", nullable = false)
private long flechaFogo = 0;

@Column(name = "flecha_veneno", nullable = false)
private long flechaVeneno = 0;

@Column(name = "flecha_diamante", nullable = false)
private long flechaDiamante = 0;

@Column(name = "flecha_ferro", nullable = false)
private long flechaFerro = 0;

@Column(name = "aljava", nullable = false)
private long aljava = 0;


@Column(name = "aljava_flecha_ativa", nullable = false)
private long aljavaFlechaAtiva = 0;


public long getInventarioArco() {
    return inventarioArco;
}

public void setInventarioArco(long inventarioArco) {
    this.inventarioArco = inventarioArco;
}

public long getArcoAtivo() {
    return arcoAtivo;
}

public void setArcoAtivo(long arcoAtivo) {
    this.arcoAtivo = arcoAtivo;
}

public long getDurabilidadeArco() {
    return durabilidadeArco;
}

public void setDurabilidadeArco(long durabilidadeArco) {
    this.durabilidadeArco = durabilidadeArco;
}

public long getFlechaFogo() {
    return flechaFogo;
}

public void setFlechaFogo(long flechaFogo) {
    this.flechaFogo = flechaFogo;
}

public long getFlechaVeneno() {
    return flechaVeneno;
}

public void setFlechaVeneno(long flechaVeneno) {
    this.flechaVeneno = flechaVeneno;
}

public long getFlechaDiamante() {
    return flechaDiamante;
}

public void setFlechaDiamante(long flechaDiamante) {
    this.flechaDiamante = flechaDiamante;
}

public long getFlechaFerro() {
    return flechaFerro;
}

public void setFlechaFerro(long flechaFerro) {
    this.flechaFerro = flechaFerro;
}

public long getAljava() {
    return aljava;
}

public void setAljava(long aljava) {
    this.aljava = aljava;
}

public long getAljavaFlechaAtiva() {
    return aljavaFlechaAtiva;
}

public void setAljavaFlechaAtiva(long aljavaFlechaAtiva) {
    this.aljavaFlechaAtiva = aljavaFlechaAtiva;
}


   
    // getters / setters
    
    
   //-------------------------------------------------------------------- 
    public long getMachadoDilacerador() {
        return machadoDilacerador;
    }

    public void setMachadoDilacerador(long machadoDilacerador) {
        this.machadoDilacerador = machadoDilacerador;
    }

    public long getMachadoDilaceradorAtivo() {
        return machadoDilaceradorAtivo;
    }

    public void setMachadoDilaceradorAtivo(long machadoDilaceradorAtivo) {
        this.machadoDilaceradorAtivo = machadoDilaceradorAtivo;
    }

    public long getMachadoDilaceradorDesgaste() {
        return machadoDilaceradorDesgaste;
    }

    public void setMachadoDilaceradorDesgaste(long machadoDilaceradorDesgaste) {
        this.machadoDilaceradorDesgaste = machadoDilaceradorDesgaste;
    }

    //-------------------------------------------------------------------
    
    public long getEspadaFlanejanteDesgaste() {
        return espadaFlanejanteDesgaste;
    }

    public void setEspadaFlanejanteDesgaste(long espadaFlanejanteDesgaste) {
        this.espadaFlanejanteDesgaste = espadaFlanejanteDesgaste;
    }

    
    public long getEspadaFlanejante() {
        return espadaFlanejante;
    }

    public void setEspadaFlanejante(long espadaflanejante) {
        this.espadaFlanejante = espadaflanejante;
    }

    public long getEspadaFlanejanteAtiva() {
        return espadaFlanejanteAtiva;
    }

    public void setEspadaFlanejanteAtiva(long espadaFlanejanteAtiva) {
        this.espadaFlanejanteAtiva = espadaFlanejanteAtiva;
    }

    //------------------------------------------------------------------
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
    private Long guerreirosInventario = 0L;
    
    //==============================================================
    @Column(name = "guerreiros_retaguarda", nullable = false)
    private Long guerreirosRetaguarda = 0L;
    
    @Column(name = "recuperacao_retaguarda", nullable = false)
    private Long recuperacaoRetaguarda = 2L;
    
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
    private Long precoGuerreiros = 10_000L;

    @Column(name = "preco_energia", nullable = false)
    private Long precoEnergia = 1_000L;

    @Column(name = "preco_ataque_especial", nullable = false)
    private Long precoAtaqueEspecial = 1_000L;
    
   @Column(name = "preco_machado_dilacerador", nullable = false)
   private Long precoMachadoDilacerador = 2_000L;
    
   @Column(name = "preco_pocao_vigor", nullable = false)
   private Long precoPocaoVigor = 1_000L;
    
   @Column(name = "preco_espada_flanejante", nullable = false)
   private Long precoEspadaFlanejante= 1_000L;


   
  //===========================================
 // GETTERS E SETTERS - LOJA
 //===========================================
   public Long getPrecoMachadoDilacerador() {
	    return precoMachadoDilacerador;
	}

	public void setPrecoMachadoDilacerador(Long precoMachadoDilacerador) {
	    this.precoMachadoDilacerador = precoMachadoDilacerador;
	}

	public Long getPrecoPocaoVigor() {
	    return precoPocaoVigor;
	}

	public void setPrecoPocaoVigor(Long precoPocaoVigor) {
	    this.precoPocaoVigor = precoPocaoVigor;
	}

	public Long getPrecoEspadaFlanejante() {
	    return precoEspadaFlanejante;
	}

	public void setPrecoEspadaFlanejante(Long precoEspadaFlanejante) {
	    this.precoEspadaFlanejante = precoEspadaFlanejante;
	}

    
   
   
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
