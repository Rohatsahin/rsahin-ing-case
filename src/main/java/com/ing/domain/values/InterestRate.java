package com.ing.domain.values;

import com.ing.domain.DomainException;

public record InterestRate(
        Double value
) {
    private static final Double MIN_INTEREST_RATE = 0.1;
    private static final Double MAX_INTEREST_RATE = 0.5;

    public InterestRate {
        if (value.compareTo(MIN_INTEREST_RATE) < 0 || value.compareTo(MAX_INTEREST_RATE) > 0) {
            throw new DomainException("interest value must be between " + MIN_INTEREST_RATE + " and " + MAX_INTEREST_RATE);
        }
    }
}
