package com.ing.infrastructure.web.model;

import com.ing.domain.commands.PayLoanCommand;

import java.math.BigDecimal;
import java.time.Instant;

public record PayCustomerLoanRequest(
        Long loanId,
        BigDecimal amount
) {

    public PayLoanCommand toCommand() {
        return new PayLoanCommand(
                loanId,
                amount,
                Instant.now()
        );
    }
}
