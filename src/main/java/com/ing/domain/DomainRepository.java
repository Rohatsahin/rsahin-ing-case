package com.ing.domain;

import java.io.Serializable;
import java.util.Optional;

public interface DomainRepository<T, ID extends Serializable> {
    Optional<T> load(ID id);

    void save(T t);
}
