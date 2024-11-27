package com.ing.infrastructure.repository;

import com.ing.domain.DomainRepository;
import com.ing.domain.loan.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepository implements DomainRepository<Customer, Long> {


    @Override
    public Optional<Customer> load(Long aLong) {
        return Optional.empty();
    }

    @Override
    public void save(Customer customer) {

    }
}
