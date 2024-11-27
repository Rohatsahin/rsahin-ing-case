package com.ing.domain.loan;

import com.ing.domain.DomainException;
import com.ing.domain.commands.CreateLoanCommand;
import com.ing.domain.values.Installment;
import com.ing.domain.values.InterestRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerLoanCreateTest {

    private Instant currentDate;

    @BeforeEach
    public void setUp() {
        currentDate = Instant.parse("2024-11-28T11:00:00.000000Z");
    }

    @Test
    void shouldCreateLoan() {
        var customer = new Customer(1L, "customer", "customer", BigDecimal.valueOf(15000), new HashMap<>());
        var command = new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.1), new Installment(12), currentDate);

        var expectedResult = customer.createLoan(command);

        expectedResult.loans().values()
                .stream().findFirst()
                .ifPresent(loan -> {
                    assertThat(loan.customerId()).isEqualByComparingTo(1L);
                    assertThat(loan.amount()).isEqualByComparingTo(BigDecimal.valueOf(10000));

                    var installments = loan.installments();

                    assertThat(installments).hasSize(12);
                    assertThat(installments.stream().map(LoanInstallment::amount).reduce(BigDecimal.ZERO, BigDecimal::add)).isEqualByComparingTo(BigDecimal.valueOf(11000.04));
                    assertThat(installments.stream().allMatch(inst -> inst.amount().equals(BigDecimal.valueOf(916.67)))).isTrue();
                    assertThat(installments.stream().allMatch(LoanInstallment::isPaid)).isFalse();
                    assertThat(installments.stream().allMatch(inst -> inst.dueDate() != null)).isTrue();
                });
    }

    @Test
    void shouldGetCustomerCreditLimits() {
        var customer = new Customer(1L, "customer", "customer", BigDecimal.valueOf(15000), new HashMap<>());
        var command = new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.1), new Installment(12), currentDate);

        var expectedResult = customer.createLoan(command);

        assertThat(expectedResult.creditLimit()).isEqualByComparingTo(BigDecimal.valueOf(15000));
        assertThat(expectedResult.getUsedCreditLimit()).isEqualByComparingTo(BigDecimal.valueOf(10000));
    }

    @Test
    void shouldNotCreateLoanWhenUserDoesNotAvailableCreditLimit() {
        var customer = new Customer(1L, "customer", "customer", BigDecimal.valueOf(15000), new HashMap<>());
        var command = new CreateLoanCommand(BigDecimal.valueOf(16000), new InterestRate(0.1), new Installment(12), currentDate);

        DomainException expectedException = assertThrows(DomainException.class, () -> customer.createLoan(command));

        assertThat(expectedException).hasMessage("insufficient credit limit");
    }

    @Test
    void shouldNotCreateLoanWhenInstallmentCountUnexpected() {
        DomainException expectedException = assertThrows(DomainException.class,
                () -> new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.1), new Installment(13), currentDate)
        );

        assertThat(expectedException).hasMessage("invalid numberOfInstallment, value: 13");
    }

    @Test
    void shouldNotCreateLoanWhenInterestRateUnexpected() {
        DomainException expectedException = assertThrows(DomainException.class,
                () -> new CreateLoanCommand(BigDecimal.valueOf(10000), new InterestRate(0.7), new Installment(12), currentDate)
        );

        assertThat(expectedException).hasMessage("interest value must be between 0.1 and 0.5");
    }
}