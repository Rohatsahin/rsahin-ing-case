package com.ing.infrastructure.persistence.relational.entitiy;

import com.ing.domain.loan.Loan;
import com.ing.domain.loan.LoanInstallment;
import com.ing.domain.values.Installment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "loan")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "number_of_installment")
    private Integer numberOfInstallment;

    @Column(name = "created_at")
    private Instant createdDate;

    @Column(name = "is_paid")
    private Boolean isPaid;

    public LoanEntity() {
    }

    public LoanEntity(Long id,
                      Long customerId,
                      BigDecimal amount,
                      Integer numberOfInstallment,
                      Instant createdDate,
                      Boolean isPaid
    ) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.numberOfInstallment = numberOfInstallment;
        this.createdDate = createdDate;
        this.isPaid = isPaid;
    }

    public static LoanEntity fromLoan(Long customerId, Loan loan) {
        return new LoanEntity(
                loan.id(),
                customerId,
                loan.amount(),
                loan.numberOfInstallment().count(),
                loan.createdDate(),
                loan.isPaid()
        );
    }

    public static Loan fromLoanEntity(Long customerId, LoanEntity loan, List<LoanInstallment> installments) {
        return new Loan(
                loan.getId(),
                customerId,
                loan.getAmount(),
                new Installment(loan.getNumberOfInstallment()),
                loan.getCreatedDate(),
                installments
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getNumberOfInstallment() {
        return numberOfInstallment;
    }

    public void setNumberOfInstallment(Integer numberOfInstallment) {
        this.numberOfInstallment = numberOfInstallment;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }
}
