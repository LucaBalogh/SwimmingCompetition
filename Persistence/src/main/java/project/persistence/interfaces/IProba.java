package project.persistence.interfaces;

import project.model.Proba;
import project.persistence.Repository;

public interface IProba  extends Repository<Long, Proba> {
    Proba findBy(String id);
    void delete(String id);
}
