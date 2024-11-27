package com.ing.domain.loan;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public record LoanInstallment(
        Long id,
        Long loanId,
        BigDecimal amount,
        Instant dueDate,
        BigDecimal paidAmount,
        Instant paymentDate
) implements Comparable<LoanInstallment> {

    // this is in fly identifier persistence managed by external systems
    private static final ThreadLocalRandom ID_IDENTIFIER = ThreadLocalRandom.current();

    private static BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.001);
    private static BigDecimal PENALTY_RATE = BigDecimal.valueOf(0.001);

    public static LoanInstallment create(Long loanId, BigDecimal amount, Instant dueDate) {
        return new LoanInstallment(ID_IDENTIFIER.nextLong(0, Long.MAX_VALUE), loanId, amount, dueDate, null, null);
    }

    // penalty = 0.001*(number of days after due date) A = P(1 + ri)
    // discount = 0.001*(number of days before due date) A = P(1 - ri)
    public Optional<LoanInstallment> pay(BigDecimal paidAmount, Instant paymentDate) {
        if (dueDate.isBefore(paymentDate)) {
            var days = ChronoUnit.DAYS.between(dueDate, paymentDate);
            var calculatedAmount = amount.multiply(BigDecimal.ONE.add(PENALTY_RATE.multiply(BigDecimal.valueOf(days))));;

            if (calculatedAmount.compareTo(paidAmount) > 0) {
                return Optional.empty();
            }

            return Optional.of(new LoanInstallment(this.id, this.loanId, this.amount, this.dueDate, calculatedAmount, paymentDate));
        } else {
            var days = ChronoUnit.DAYS.between(paymentDate, dueDate);
            var calculatedAmount = amount.multiply(BigDecimal.ONE.subtract(DISCOUNT_RATE.multiply(BigDecimal.valueOf(days))));

            if (calculatedAmount.compareTo(paidAmount) > 0) {
                return Optional.empty();
            }

            return Optional.of(new LoanInstallment(this.id, this.loanId, this.amount, this.dueDate, calculatedAmount, paymentDate));
        }
    }

    Boolean isPaid() {
        return paidAmount != null && paymentDate != null;
    }

    @Override
    public int compareTo(LoanInstallment o) {
        return this.dueDate.compareTo(o.dueDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LoanInstallment that = (LoanInstallment) o;

        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
