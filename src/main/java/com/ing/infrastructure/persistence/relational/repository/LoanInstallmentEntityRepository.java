package com.ing.infrastructure.persistence.relational.repository;

import com.ing.infrastructure.persistence.relational.entitiy.LoanInstallmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanInstallmentEntityRepository extends CrudRepository<LoanInstallmentEntity, Long> {

    List<LoanInstallmentEntity> findAllByLoanId(Long loanId);
}
