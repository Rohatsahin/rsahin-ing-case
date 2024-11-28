package com.ing.infrastructure.web.model;

import com.ing.domain.commands.results.CustomerPaymentResult;

import java.math.BigDecimal;

public record PayCustomerLoanResponse(
        Integer numberOfPaidInstallments,
        BigDecimal amount,
        BigDecimal totalAmountSpent
) {
    public PayCustomerLoanResponse(CustomerPaymentResult paymentResult) {
        this(paymentResult.numberOfPaidInstallments(), paymentResult.amount(), paymentResult.totalAmountSpent());
    }
}
