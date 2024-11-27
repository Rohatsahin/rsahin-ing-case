package com.ing.domain.values;

import com.ing.domain.DomainException;

import java.util.Arrays;
import java.util.List;

public record Installment(
        Integer count
) {
    private static final List<Integer> availableInstallments = Arrays.asList(6, 9, 12, 24);

    public Installment {
        if (!availableInstallments.contains(count)) {
            throw new DomainException("Invalid installment, value: " + count);
        }
    }
}
