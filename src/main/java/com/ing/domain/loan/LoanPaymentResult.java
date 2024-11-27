package com.ing.domain.loan;

import java.math.BigDecimal;

public record LoanPaymentResult(
        Loan loan,
        Integer numberOfPaidInstallments,
        BigDecimal amount,
        BigDecimal totalAmountSpent
) {
}
