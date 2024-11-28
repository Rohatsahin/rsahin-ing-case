package com.ing.infrastructure.persistence.relational.repository;

import com.ing.infrastructure.persistence.relational.entitiy.LoanEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanEntityRepository extends CrudRepository<LoanEntity, Long> {

    List<LoanEntity> findByCustomerId(Long customerId);
}
