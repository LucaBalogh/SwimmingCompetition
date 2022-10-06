package project.model;
import java.io.Serializable;

public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 4500794925126064682L;
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}