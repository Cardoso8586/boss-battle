package com.boss_battle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
public class BossBattleTransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String currency;

    @Column(nullable = false, length = 255)
    private String amount;

   

    @Column(nullable = false, length = 255)
    private String email; 

    @Column(length = 255)
    private String note;

    @Column(length = 255)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BossBattleTransactionHistory() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

   

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
