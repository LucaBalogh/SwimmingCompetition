package project.persistence.interfaces;

import project.model.Inscriere;
import project.model.Participant;
import project.persistence.Repository;

import java.util.List;

public interface IParticipant extends Repository<Long, Participant> {
    List<Inscriere> findAllByProba(String proba);
    Participant findBy(String username, String pass);
}
