package com.boss_battle.dto;

public class UsuarioMissaoDTO {

    private Long id;
    private String nomeMissao;

    private String periodo;
    private String categoria;

    private Long progressoAtual;
    private Long objetivoAtual;

    private boolean podeResgatar;

    private boolean concluida;
    
    private Long recompensa;
    private String itemRecompensa;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeMissao() {
        return nomeMissao;
    }

    public void setNomeMissao(String nomeMissao) {
        this.nomeMissao = nomeMissao;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getProgressoAtual() {
        return progressoAtual;
    }

    public void setProgressoAtual(Long progressoAtual) {
        this.progressoAtual = progressoAtual;
    }

    public Long getObjetivoAtual() {
        return objetivoAtual;
    }

    public void setObjetivoAtual(Long objetivoAtual) {
        this.objetivoAtual = objetivoAtual;
    }

    public boolean isPodeResgatar() {
        return podeResgatar;
    }

    public void setPodeResgatar(boolean podeResgatar) {
        this.podeResgatar = podeResgatar;
    }

	public boolean isConcluida() {
		return concluida;
	}

	public void setConcluida(boolean concluida) {
		this.concluida = concluida;
	}

	public Long getRecompensa() {
		return recompensa;
	}

	public void setRecompensa(Long recompensa) {
		this.recompensa = recompensa;
	}

	public String getItemRecompensa() {
		return itemRecompensa;
	}

	public void setItemRecompensa(String itemRecompensa) {
		this.itemRecompensa = itemRecompensa;
	}
}