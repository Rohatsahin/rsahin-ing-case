package com.ing.domain.commands;

import com.ing.domain.values.Installment;
import com.ing.domain.values.InterestRate;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateLoanCommand(
        BigDecimal amount,
        InterestRate rate,
        Installment installment,
        Instant creationDate
) {
}
