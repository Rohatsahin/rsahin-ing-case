package com.ing.domain.commands;


import java.math.BigDecimal;
import java.time.Instant;

public record PayLoanCommand(
       Long loanId,
       BigDecimal inCome,
       Instant paymentDate
) {
}

