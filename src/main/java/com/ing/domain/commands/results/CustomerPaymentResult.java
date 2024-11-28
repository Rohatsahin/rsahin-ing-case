package com.ing.domain.commands.results;

import com.ing.domain.loan.Customer;

import java.math.BigDecimal;

public record CustomerPaymentResult(
        Customer customer,
        Long paidLoanId,
        Integer numberOfPaidInstallments,
        BigDecimal amount,
        BigDecimal totalAmountSpent
) {
}
