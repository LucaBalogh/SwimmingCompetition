package project.model;


public class ProbaDTO extends Entity<Long> {

    private String distanta;
    private String stil;
    private Integer number;

    public ProbaDTO(String distanta, String stil, Integer number) {
        this.distanta = distanta;
        this.stil = stil;
        this.number = number;
    }

    public String getDistanta() {
        return distanta;
    }

    public void setDistanta(String distanta) {
        this.distanta = distanta;
    }

    public String getStil() {
        return stil;
    }

    public void setStil(String stil) {
        this.stil = stil;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ProbaDTO: " +  getDistanta() + " " + getStil() + " " + getNumber();
    }

}

