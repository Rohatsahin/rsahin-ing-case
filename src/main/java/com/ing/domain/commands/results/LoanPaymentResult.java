package com.ing.domain.commands.results;

import com.ing.domain.loan.Loan;

import java.math.BigDecimal;

public record LoanPaymentResult(
        Loan loan,
        Integer numberOfPaidInstallments,
        BigDecimal amount,
        BigDecimal totalAmountSpent
) {
}
