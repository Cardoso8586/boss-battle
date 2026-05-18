package com.boss_battle.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "depositos_boss_coins")
public class DepositoBossCoins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    @Column(unique = true)
    private String paymentId;

    private String orderId;

    private String moeda;

    @Column(precision = 19, scale = 2)
    private BigDecimal valorUsd;

    @Column(precision = 19, scale = 8)
    private BigDecimal valorCripto;

    @Column(length = 500)
    private String enderecoCarteira;

    private String status;

    private boolean creditado = false;

    private LocalDateTime criadoEm = LocalDateTime.now();

    private LocalDateTime atualizadoEm;

    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public BigDecimal getValorUsd() {
        return valorUsd;
    }

    public void setValorUsd(BigDecimal valorUsd) {
        this.valorUsd = valorUsd;
    }

    public BigDecimal getValorCripto() {
        return valorCripto;
    }

    public void setValorCripto(BigDecimal valorCripto) {
        this.valorCripto = valorCripto;
    }

    public String getEnderecoCarteira() {
        return enderecoCarteira;
    }

    public void setEnderecoCarteira(String enderecoCarteira) {
        this.enderecoCarteira = enderecoCarteira;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCreditado() {
        return creditado;
    }

    public void setCreditado(boolean creditado) {
        this.creditado = creditado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}