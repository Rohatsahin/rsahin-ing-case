package com.ing.domain.values;

import com.ing.domain.DomainException;

public record InterestRate(
        Double value
) {
    private static final Double minInterestRate = 0.1;
    private static final Double maxInterestRate = 0.5;

    public InterestRate {
        if (value.compareTo(minInterestRate) < 0 || value.compareTo(maxInterestRate) > 0) {
            throw new DomainException("interest value must be between " + minInterestRate + " and " + maxInterestRate);
        }
    }
}
