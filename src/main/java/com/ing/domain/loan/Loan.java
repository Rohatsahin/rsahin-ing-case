package com.ing.domain.loan;

import com.ing.domain.values.Installment;
import com.ing.domain.values.InterestRate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Loan(
        Long id,
        Long customerId,
        BigDecimal amount,
        Installment installment,
        Instant createdDate,

        // Loan Associations
        Collection<LoanInstallment> installments
) {

    private static final Integer MAXIMUM_PAID_INSTALLMENT_IN_SAME_TIME = 3;

    public static Loan create(Long customerId, BigDecimal amount, Installment installment, InterestRate rate) {
        var creationDate = Instant.now();
        var totalAmount = amount.multiply(BigDecimal.valueOf(1 + rate.value()));
        var installmentAmount = totalAmount.divide(BigDecimal.valueOf(installment.count()), RoundingMode.CEILING);

        var installments = IntStream.range(1, installment.count())
                .mapToObj(inst -> {
                    var creationLocalDate = creationDate.atZone(ZoneOffset.UTC).toLocalDate();
                    var installmentDueDate = creationLocalDate.withMonth(creationLocalDate.getMonthValue() + inst)
                            .withDayOfMonth(1)
                            .atStartOfDay()
                            .toInstant(ZoneOffset.UTC);

                    return LoanInstallment.create(null, installmentAmount, installmentDueDate);
                }).toList();

        return new Loan(null, customerId, amount, installment, creationDate, installments);
    }


    LoanPaymentResult payInstallments(BigDecimal inCome, Instant paymentDate) {
        if (isPaid()) {
            return new LoanPaymentResult(this, 0, inCome, BigDecimal.ZERO);
        }

        var paidInstallments = new HashMap<Long, LoanInstallment>();

        for (LoanInstallment installment : availableInstallments()) {
            var paidInstallmentsAmount = paidInstallments.values().stream().map(LoanInstallment::paidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            var paymentAmount = inCome.subtract(paidInstallmentsAmount);

            var paymentResult = installment.pay(paymentAmount, paymentDate);

            if (paymentResult.isPresent()) {
                paidInstallments.put(installment.id(), installment);
            } else {
                break;
            }
        }

        var finalizedInstallments = installments.stream()
                .map(installment -> paidInstallments.getOrDefault(installment.id(), installment))
                .toList();
        var numberOfPaidInstallments = paidInstallments.values().size();
        var totalAmountSpent = paidInstallments.values().stream().map(LoanInstallment::paidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LoanPaymentResult(
                new Loan(id, customerId, amount, installment, createdDate, finalizedInstallments),
                numberOfPaidInstallments,
                inCome,
                totalAmountSpent
        );
    }


    List<LoanInstallment> availableInstallments() {
        return installments.stream()
                .sorted()
                .filter(installment -> !installment.isPaid())
                .collect(Collectors.toCollection(LinkedList::new))
                .subList(0, MAXIMUM_PAID_INSTALLMENT_IN_SAME_TIME);
    }

    Boolean isPaid() {
        return installments.stream()
                .allMatch(LoanInstallment::isPaid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Loan loan = (Loan) o;

        return new EqualsBuilder().append(id, loan.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
