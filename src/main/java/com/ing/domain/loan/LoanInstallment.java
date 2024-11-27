package com.ing.domain.loan;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public record LoanInstallment(
        Long id,
        Long loanId,
        BigDecimal amount,
        Instant dueDate,
        BigDecimal paidAmount,
        Instant paymentDate
) implements Comparable<LoanInstallment> {

    private static BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.001);
    private static BigDecimal PENALTY_RATE = BigDecimal.valueOf(0.001);

    public static LoanInstallment create(Long loanId, BigDecimal amount, Instant dueDate) {
        return new LoanInstallment(0L, loanId, amount, dueDate, null, null);
    }

    public Optional<LoanInstallment> pay(BigDecimal paidAmount, Instant paymentDate) {
        if (dueDate.isBefore(paymentDate)) {
            var days = ChronoUnit.DAYS.between(dueDate, paymentDate);
            var penalty = amount.multiply(PENALTY_RATE.multiply(BigDecimal.valueOf(days)));
            var calculatedAmount = amount.add(penalty);

            if (calculatedAmount.compareTo(paidAmount) < 0) {
                return Optional.empty();
            }

            return Optional.of(new LoanInstallment(this.id, this.loanId, this.amount, this.dueDate, calculatedAmount, paymentDate));
        } else {
            var days = ChronoUnit.DAYS.between(paymentDate, dueDate);
            var discount = amount.multiply(DISCOUNT_RATE.multiply(BigDecimal.valueOf(days)));
            var calculatedAmount = amount.subtract(discount);

            if (calculatedAmount.compareTo(paidAmount) < 0) {
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
        return o.dueDate.compareTo(this.dueDate);
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
