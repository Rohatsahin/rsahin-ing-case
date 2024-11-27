package com.ing.domain.loan;

import com.ing.domain.commands.CreateLoanCommand;
import com.ing.domain.commands.PayLoanCommand;
import com.ing.domain.values.Installment;
import com.ing.domain.values.InterestRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerLoanPaymentTest {

    private Instant currentDate;

    @BeforeEach
    public void setUp() {
        currentDate = Instant.parse("2024-11-28T11:00:00.000000Z");
    }

    @Test
    void shouldCustomerCreateLoanPayment() {
        var customer = new Customer(1L, "customer", "customer", BigDecimal.valueOf(15000), new HashMap<>())
                .createLoan(new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.1), new Installment(6), currentDate));
        Long loanId = customer.loans().values().stream().findFirst().get().id();

        var paymentCommand = new PayLoanCommand(loanId, BigDecimal.valueOf(10000), currentDate);
        var expectedPayment = customer.payLoan(paymentCommand);

        assertThat(expectedPayment.numberOfPaidInstallments()).isEqualTo(3);
        assertThat(expectedPayment.totalAmountSpent()).isEqualByComparingTo(BigDecimal.valueOf(4835.00967));
    }

    @Test
    void shouldNotCreateLoanPaymentWhenAmountInsufficient() {
        var customer = new Customer(1L, "customer", "customer", BigDecimal.valueOf(15000), new HashMap<>())
                .createLoan(new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.1), new Installment(6), currentDate));
        Long loanId = customer.loans().values().stream().findFirst().get().id();

        var paymentCommand = new PayLoanCommand(loanId, BigDecimal.valueOf(1000), currentDate);
        var expectedPayment = customer.payLoan(paymentCommand);

        assertThat(expectedPayment.numberOfPaidInstallments()).isEqualTo(0);
        assertThat(expectedPayment.totalAmountSpent()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCreateCompleteLoanPayment() {
        var customer = new Customer(1L, "customer", "customer", BigDecimal.valueOf(15000), new HashMap<>())
                .createLoan(new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.1), new Installment(6), currentDate));
        Long loanId = customer.loans().values().stream().findFirst().get().id();

        // first three payment
        var paymentCommandOne = new PayLoanCommand(loanId, BigDecimal.valueOf(5000), currentDate);
        var paymentOne = customer.payLoan(paymentCommandOne);

        assertThat(paymentOne.customer().loans().get(loanId).isPaid()).isFalse();
        assertThat(paymentOne.numberOfPaidInstallments()).isEqualTo(3);
        assertThat(paymentOne.totalAmountSpent()).isEqualByComparingTo(BigDecimal.valueOf(4835.00967));

        // second three payment and complete loan
        var paymentCommandTwo = new PayLoanCommand(loanId, BigDecimal.valueOf(5000), currentDate);
        var expectedResultPayment = paymentOne.customer().payLoan(paymentCommandTwo);

        assertThat(expectedResultPayment.customer().loans().get(loanId).isPaid()).isTrue();
        assertThat(expectedResultPayment.numberOfPaidInstallments()).isEqualTo(3);
        assertThat(expectedResultPayment.totalAmountSpent()).isEqualByComparingTo(BigDecimal.valueOf(4386.67544));
    }
}
