package com.ing.service;

import com.ing.domain.DomainException;
import com.ing.domain.commands.CreateLoanCommand;
import com.ing.domain.commands.PayLoanCommand;
import com.ing.domain.loan.CustomerPaymentResult;
import com.ing.domain.loan.Loan;
import com.ing.infrastructure.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CustomerLoanService {

    private final CustomerRepository customerRepository;

    public CustomerLoanService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createLoan(Long customerId, CreateLoanCommand command) {
        var customer = customerRepository.load(customerId);

        if (customer.isPresent()) {
            var result = customer.get().createLoan(command);
            customerRepository.save(result);
        }

        throw new DomainException("Customer not found");
    }

    public HashMap<Long, Loan> getLoans(Long customerId) {
        var customer = customerRepository.load(customerId);

        if (customer.isPresent()) {
            return customer.get().loans();
        }

        throw new DomainException("Customer not found");
    }

    public CustomerPaymentResult payLoan(Long customerId, PayLoanCommand command) {
        var customer = customerRepository.load(customerId);

        if (customer.isPresent()) {
            var result = customer.get().payLoan(command);
            customerRepository.save(result.customer());
            return result;
        }

        throw new DomainException("Customer not found");
    }
}
