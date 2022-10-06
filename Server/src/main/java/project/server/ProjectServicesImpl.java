package project.server;

import project.model.Inscriere;
import project.model.Participant;
import project.model.Proba;
import project.model.ProbaDTO;
import project.persistence.interfaces.IInscriere;
import project.persistence.interfaces.IParticipant;
import project.persistence.interfaces.IProba;
import project.services.IProjectObserver;
import project.services.IProjectServices;
import project.services.ProjectException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ProjectServicesImpl implements IProjectServices {

    private IParticipant partRepo;
    private IInscriere insRepo;
    private IProba prRepo;
    private Map<String, IProjectObserver> loggedParticipants;

    public ProjectServicesImpl(IParticipant partRepo, IProba prRepo, IInscriere insRepo) {

        this.partRepo = partRepo;
        this.prRepo = prRepo;
        this.insRepo = insRepo;
        loggedParticipants=new ConcurrentHashMap<>();;
    }


    public synchronized void login(Participant user, IProjectObserver client) throws ProjectException {
        Participant userR = partRepo.findBy(user.getName(),user.getPasswd());
        if (userR!=null){
            if(loggedParticipants.get(user.getName())!=null)
                throw new ProjectException("User already logged in.");
            loggedParticipants.put(user.getName(), client);
        }else
            throw new ProjectException("Authentication failed.");
    }

    public synchronized void addInscriere(Inscriere ins) throws ProjectException {
        IProjectObserver receiverClient = loggedParticipants.get(ins.getParticipant());
        IProjectObserver sv = null;
        if (receiverClient!=null) {
            insRepo.add(ins);
            List<Inscriere> iList = StreamSupport.stream(insRepo.findAll().spliterator(), false)
                    .collect(Collectors.toList());
            List<ProbaDTO> prList = StreamSupport.stream(getProbe().spliterator(), false)
                    .collect(Collectors.toList());
            for(String s : loggedParticipants.keySet()) {
                sv = loggedParticipants.get(s);
                try {
                    sv.savedInscriere(iList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    sv.savedProbe(prList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        else
            throw new ProjectException("User  is  not logged in.");
    }

    public synchronized void logout(Participant user, IProjectObserver client) throws ProjectException {
        IProjectObserver localClient = loggedParticipants.remove(user.getName());
        if (localClient==null)
            throw new ProjectException("User "+user.getName()+" is not logged in.");
    }

    public synchronized List<ProbaDTO> getProbe(){
        List<ProbaDTO> pD = new ArrayList<>();
        for(Proba p : prRepo.findAll())
        {
            ProbaDTO prD = new ProbaDTO(p.getDistanta().toString(),p.getStil().toString(),getNrProbe(p));
            prD.setId(p.getId());
            pD.add(prD);
        }
        return pD;
    }

    public int getNrProbe(Proba p){
        int count = 0;
        for(Inscriere i : insRepo.findAll()){
            String[] s = i.getProba().split(" - ");
            if(p.getDistanta().toString().equals(s[0]) && p.getStil().toString().equals(s[1]))
                count++;
        }
        return count;
    }

    public synchronized Iterable<Inscriere> getInscrieri(){
        return insRepo.findAll();
    }

    public synchronized List<Inscriere> getByProba(String p){
        return partRepo.findAllByProba(p);
    }
}
