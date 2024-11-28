package com.ing.infrastructure.web.model;

import com.ing.domain.commands.PayLoanCommand;

import java.math.BigDecimal;
import java.time.Instant;

public record PayCustomerLoanRequest(
        BigDecimal amount
) {

    public PayLoanCommand toCommand(Long loanId) {
        return new PayLoanCommand(
                loanId,
                amount,
                Instant.now()
        );
    }
}
