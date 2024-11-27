package com.ing.domain.loan;

import java.math.BigDecimal;

public record CustomerPaymentResult(
        Customer customer,
        Long paidLoanId,
        Integer numberOfPaidInstallments,
        BigDecimal amount,
        BigDecimal totalAmountSpent
) {
}
