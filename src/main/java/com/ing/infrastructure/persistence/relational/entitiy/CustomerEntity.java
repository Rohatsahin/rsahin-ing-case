package com.ing.infrastructure.persistence.relational.entitiy;

import com.ing.domain.loan.Customer;
import com.ing.domain.loan.Loan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "used_credit_limit")
    private BigDecimal usedCreditLimit;

    public CustomerEntity() {
    }

    public CustomerEntity(Long id,
                          String name,
                          String surname,
                          BigDecimal creditLimit,
                          BigDecimal usedCreditLimit
    ) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.creditLimit = creditLimit;
        this.usedCreditLimit = usedCreditLimit;
    }

    public static CustomerEntity fromCustomer(Customer customer) {
        return new CustomerEntity(
                customer.id(),
                customer.name(),
                customer.surname(),
                customer.creditLimit(),
                customer.getUsedCreditLimit()
        );
    }

    public static Customer fromCustomerEntity(CustomerEntity entity, List<Loan> loans) {
        return new Customer(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getCreditLimit(),
                new HashMap<>(loans.stream().collect(Collectors.toMap(Loan::id, loan -> loan)))
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getUsedCreditLimit() {
        return usedCreditLimit;
    }

    public void setUsedCreditLimit(BigDecimal usedCreditLimit) {
        this.usedCreditLimit = usedCreditLimit;
    }
}
