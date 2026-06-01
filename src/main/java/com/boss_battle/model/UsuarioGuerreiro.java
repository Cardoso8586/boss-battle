package com.boss_battle.model;

import java.time.LocalDate;

import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

@Entity
public class UsuarioGuerreiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UsuarioBossBattle usuario;

    @ManyToOne
    private Guerreiro guerreiro;

    private Long quantidade = 0L;

    private Long nivel = 1L;

    private LocalDate criadoEm = LocalDate.now();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioBossBattle getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBossBattle usuario) {
        this.usuario = usuario;
    }

    public Guerreiro getGuerreiro() {
        return guerreiro;
    }

    public void setGuerreiro(Guerreiro guerreiro) {
        this.guerreiro = guerreiro;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }

}