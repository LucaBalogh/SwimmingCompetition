package project.services;

import project.model.Inscriere;
import project.model.ProbaDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IProjectObserver extends Remote {
     void savedInscriere(List<Inscriere> ins) throws ProjectException, RemoteException;
     void savedProbe(List<ProbaDTO> pr) throws ProjectException,RemoteException;
}
