package com.ing.infrastructure.persistence.relational;

import com.ing.domain.DomainRepository;
import com.ing.domain.loan.Customer;
import com.ing.infrastructure.persistence.relational.repository.CustomerEntityRepository;
import com.ing.infrastructure.persistence.relational.repository.LoanEntityRepository;
import com.ing.infrastructure.persistence.relational.repository.LoanInstallmentEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ing.infrastructure.persistence.relational.entitiy.CustomerEntity.fromCustomer;
import static com.ing.infrastructure.persistence.relational.entitiy.CustomerEntity.fromCustomerEntity;
import static com.ing.infrastructure.persistence.relational.entitiy.LoanEntity.fromLoan;
import static com.ing.infrastructure.persistence.relational.entitiy.LoanEntity.fromLoanEntity;
import static com.ing.infrastructure.persistence.relational.entitiy.LoanInstallmentEntity.formInstallment;
import static com.ing.infrastructure.persistence.relational.entitiy.LoanInstallmentEntity.formInstallmentEntity;

@Repository
public class CustomerRepository implements DomainRepository<Customer, Long> {

    private final CustomerEntityRepository customerEntityRepository;

    private final LoanEntityRepository loanEntityRepository;

    private final LoanInstallmentEntityRepository loanInstallmentEntityRepository;

    public CustomerRepository(CustomerEntityRepository customerEntityRepository,
                              LoanEntityRepository loanEntityRepository,
                              LoanInstallmentEntityRepository loanInstallmentEntityRepository) {
        this.customerEntityRepository = customerEntityRepository;
        this.loanEntityRepository = loanEntityRepository;
        this.loanInstallmentEntityRepository = loanInstallmentEntityRepository;
    }

    @Override
    public Optional<Customer> load(Long customerId) {
        return customerEntityRepository.findById(customerId).map(customer -> {
            var loans = loanEntityRepository.findByCustomerId(customerId).stream()
                    .map(ln -> {
                        var installments = loanInstallmentEntityRepository.findAllByLoanId(ln.getId());

                        return fromLoanEntity(
                                customerId,
                                ln,
                                installments.stream().map(inst -> formInstallmentEntity(ln.getId(), inst)).toList()
                        );
                    }).toList();

            return fromCustomerEntity(customer, loans);
        });
    }

    @Transactional
    @Override
    public void save(Customer customer) {
        var savedCustomer = customerEntityRepository.save(fromCustomer(customer));

        customer.loans().values()
                .forEach(loan -> {
                    var savedLoan = loanEntityRepository.save(fromLoan(savedCustomer.getId(), loan));

                    loan.installments().forEach(
                            installment -> loanInstallmentEntityRepository.save(formInstallment(savedLoan.getId(), installment))
                    );
                });
    }
}
