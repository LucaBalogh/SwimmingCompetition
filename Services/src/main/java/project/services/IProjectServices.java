package project.services;

import project.model.Inscriere;
import project.model.Participant;
import project.model.ProbaDTO;

import java.util.List;


public interface IProjectServices {
     void login(Participant user, IProjectObserver client) throws ProjectException;
     void addInscriere(Inscriere ins) throws ProjectException;
     void logout(Participant user, IProjectObserver client) throws ProjectException;
     List<ProbaDTO> getProbe() throws ProjectException;
     Iterable<Inscriere> getInscrieri() throws ProjectException;
     List<Inscriere> getByProba(String s) throws ProjectException;
}
