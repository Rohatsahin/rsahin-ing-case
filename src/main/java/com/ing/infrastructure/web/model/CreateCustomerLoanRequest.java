package com.ing.infrastructure.web.model;

import com.ing.domain.commands.CreateLoanCommand;
import com.ing.domain.values.Installment;
import com.ing.domain.values.InterestRate;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCustomerLoanRequest(
        BigDecimal amount,
        Double interestRate,
        Integer installmentsCount
) {

    public CreateLoanCommand toCommand() {
        return new CreateLoanCommand(
                amount,
                new InterestRate(interestRate),
                new Installment(installmentsCount),
                Instant.now()
        );
    }
}
