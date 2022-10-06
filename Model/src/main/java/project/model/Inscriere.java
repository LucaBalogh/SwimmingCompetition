package project.model;

public class Inscriere extends Entity<Long> {
    private String participant;
    private String proba;

    public Inscriere(String participant, String proba) {
        this.participant = participant;
        this.proba = proba;
    }

    public Inscriere(){}

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getProba() {
        return proba;
    }

    public void setProba(String proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return "Inscriere: " + " " +  " Participant: " + participant + " " +  " Proba: " + proba;
    }
}
