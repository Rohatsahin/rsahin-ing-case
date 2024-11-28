package com.ing.domain.values;

import com.ing.domain.DomainException;

import java.util.Arrays;
import java.util.List;

public record Installment(
        Integer count
) {
    private static final List<Integer> AVAILABLE_INSTALLMENTS = Arrays.asList(6, 9, 12, 24);
    private static final Integer YEAR_MONTH_COUNT = 12;

    public Installment {
        if (!AVAILABLE_INSTALLMENTS.contains(count)) {
            throw new DomainException("invalid numberOfInstallment, value: " + count);
        }
    }

    public Double monthToYear() {
        return ((double) count / YEAR_MONTH_COUNT);
    }
}
