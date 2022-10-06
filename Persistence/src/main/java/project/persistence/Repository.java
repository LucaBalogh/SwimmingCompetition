package project.persistence;

import project.model.Entity;

public interface Repository<ID, E extends Entity<ID>> {
    void add(E elem);
    void update(ID id, E elem);
    Iterable<E> findAll();
}

