package com.ing.infrastructure.web;

import com.ing.domain.loan.Loan;
import com.ing.infrastructure.web.model.CreateCustomerLoanRequest;
import com.ing.infrastructure.web.model.PayCustomerLoanRequest;
import com.ing.infrastructure.web.model.PayCustomerLoanResponse;
import com.ing.service.CustomerLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/v1/customers/{customerId}/loans")
public class CustomerLoanController {

    private final CustomerLoanService customerLoanService;

    public CustomerLoanController(CustomerLoanService customerLoanService) {
        this.customerLoanService = customerLoanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("(hasRole('CUSTOMER') and hasAuthority('op_customer_' + #customerId)) or hasRole('ADMIN')")
    public void createLoan(@PathVariable("customerId") Long customerId, @RequestBody CreateCustomerLoanRequest request) {
        customerLoanService.createLoan(customerId, request.toCommand());
    }

    @PostMapping("{loanId}/pay")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("(hasRole('CUSTOMER') and hasAuthority('op_customer_' + #customerId)) or hasRole('ADMIN')")
    public ResponseEntity<PayCustomerLoanResponse> payLoan(@PathVariable("customerId") Long customerId, @PathVariable("loanId") Long loanId,
                                                           @RequestBody PayCustomerLoanRequest request) {
        var paymentResult = customerLoanService.payLoan(customerId, request.toCommand(loanId));
        return ResponseEntity.ofNullable(new PayCustomerLoanResponse(paymentResult));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("(hasRole('CUSTOMER') and hasAuthority('op_customer_' + #customerId)) or hasRole('ADMIN')")
    public ResponseEntity<Collection<Loan>> getLoans(@PathVariable("customerId") Long customerId) {
        var loans = customerLoanService.getLoans(customerId);
        return ResponseEntity.ofNullable(loans.values());
    }
}
