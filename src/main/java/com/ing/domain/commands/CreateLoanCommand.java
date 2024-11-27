package com.ing.domain.commands;

import com.ing.domain.values.Installment;
import com.ing.domain.values.InterestRate;

import java.math.BigDecimal;

public record CreateLoanCommand(
        BigDecimal amount,
        InterestRate rate,
        Installment installment
) {
}
