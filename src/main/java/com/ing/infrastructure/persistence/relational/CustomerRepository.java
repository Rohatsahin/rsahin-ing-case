package com.ing.infrastructure.persistence.relational;

import com.ing.domain.DomainRepository;
import com.ing.domain.loan.Customer;
import com.ing.domain.loan.Loan;
import com.ing.domain.loan.LoanInstallment;
import com.ing.domain.values.Installment;
import com.ing.infrastructure.persistence.relational.entitiy.CustomerEntity;
import com.ing.infrastructure.persistence.relational.entitiy.LoanEntity;
import com.ing.infrastructure.persistence.relational.entitiy.LoanInstallmentEntity;
import com.ing.infrastructure.persistence.relational.repository.CustomerEntityRepository;
import com.ing.infrastructure.persistence.relational.repository.LoanEntityRepository;
import com.ing.infrastructure.persistence.relational.repository.LoanInstallmentEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

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

                        return new Loan(
                                ln.getId(),
                                ln.getCustomerId(),
                                ln.getAmount(),
                                new Installment(ln.getNumberOfInstallment()),
                                ln.getCreatedDate(),
                                installments.stream()
                                        .map(inst -> new LoanInstallment(
                                                        inst.getId(), inst.getLoanId(), inst.getAmount(),
                                                        inst.getDueDate(), inst.getPaidAmount(), inst.getPaymentDate()
                                                )
                                        ).toList()
                        );
                    }).collect(Collectors.toMap(Loan::id, loan -> loan));

            return new Customer(
                    customer.getId(),
                    customer.getName(),
                    customer.getSurname(),
                    customer.getCreditLimit(),
                    new HashMap<>(loans));
        });
    }

    @Transactional
    @Override
    public void save(Customer customer) {
        var customerEntity = new CustomerEntity(
                customer.id(),
                customer.name(),
                customer.surname(),
                customer.creditLimit(),
                customer.getUsedCreditLimit()
        );

        customerEntityRepository.save(customerEntity);

        customer.loans().values().forEach(loan -> {
            LoanEntity savedLoan = loanEntityRepository.save(new LoanEntity(
                    loan.id(),
                    loan.customerId(),
                    loan.amount(),
                    loan.numberOfInstallment().count(),
                    loan.createdDate(),
                    loan.isPaid()
            ));

            loan.installments().forEach(installment -> {
                loanInstallmentEntityRepository.save(new LoanInstallmentEntity(
                        installment.id(),
                        savedLoan.getId(),
                        installment.amount(),
                        installment.dueDate(),
                        installment.paidAmount(),
                        installment.paymentDate(),
                        installment.isPaid()
                ));
            });
        });
    }
}
