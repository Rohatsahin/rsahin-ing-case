package com.ing.domain.loan;

import com.ing.domain.commands.CreateLoanCommand;
import com.ing.domain.commands.PayLoanCommand;
import com.ing.domain.DomainException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.HashMap;

public record Customer(
        Long id,
        String name,
        String surname,
        BigDecimal creditLimit,

        // Customer Loan Space
        HashMap<Long, Loan> loans
) {

    public Customer createLoan(CreateLoanCommand command) {
        var availableCreditLimit = creditLimit.subtract(getUsedCreditLimit());

        if (availableCreditLimit.compareTo(command.amount()) < 0) {
            throw new DomainException("insufficient credit limit");
        }


        var customerLoans = new HashMap<>(loans);
        customerLoans.put(null, Loan.create(this.id, command.amount(), command.installment(), command.rate()));

        return new Customer(id, name, surname, creditLimit, customerLoans);
    }

    public CustomerPaymentResult payLoan(PayLoanCommand command) {
        var customerLoans = new HashMap<>(loans);
        var loan = customerLoans.get(command.loanId());

        if (loan != null) {
            LoanPaymentResult paymentResult = loan.payInstallments(command.inCome(), command.paymentDate());

            var finalizedLoans = new HashMap<>(customerLoans);
            finalizedLoans.put(command.loanId(), paymentResult.loan());

            return new CustomerPaymentResult(
                    new Customer(id, name, surname, creditLimit, finalizedLoans),
                    command.loanId(),
                    paymentResult.numberOfPaidInstallments(),
                    command.inCome(),
                    paymentResult.totalAmountSpent()
            );
        } else {
            throw new DomainException("loan with id " + command.loanId() + " not found");
        }
    }

    BigDecimal getUsedCreditLimit() {
        return loans.values().stream().map(Loan::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return new EqualsBuilder().append(id, customer.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
