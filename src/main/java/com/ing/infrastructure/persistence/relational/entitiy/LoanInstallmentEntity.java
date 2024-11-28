package com.ing.infrastructure.persistence.relational.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "loan_installment")
public class LoanInstallmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_id")
    private Long loanId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Column(name = "is_paid")
    private Boolean isPaid;

    public LoanInstallmentEntity() {
    }

    public LoanInstallmentEntity(Long id, Long loanId, BigDecimal amount, Instant dueDate, BigDecimal paidAmount, Instant paymentDate, Boolean isPaid) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paidAmount = paidAmount;
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }
}
