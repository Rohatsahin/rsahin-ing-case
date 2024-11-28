package com.ing.infrastructure.persistence.relational.repository;

import com.ing.infrastructure.persistence.relational.entitiy.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerEntityRepository extends CrudRepository<CustomerEntity, Long> {
}
