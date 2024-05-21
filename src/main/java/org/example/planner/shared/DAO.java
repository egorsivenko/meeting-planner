package org.example.planner.shared;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {

    List<T> listAll();

    Optional<T> getById(ID id);

    ID create(T t);

    void update(T t);

    void delete(ID id);
}
