package project.model;


public class Proba extends Entity<Long> {

    private Distanta distanta;
    private Stil stil;

    public Proba(Distanta distanta, Stil stil) {
        this.distanta = distanta;
        this.stil = stil;
    }

    public Proba(){}

    public Distanta getDistanta() {
        return distanta;
    }

    public void setDistanta(Distanta distanta) {
        this.distanta = distanta;
    }

    public Stil getStil() {
        return stil;
    }

    public void setStil(Stil stil) {
        this.stil = stil;
    }

    @Override
    public String toString() {
        return "Proba: " +  getDistanta() + " + " + getStil();
    }

}
