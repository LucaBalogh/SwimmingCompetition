package project.model;

import java.util.Objects;
import java.util.Random;

public class Participant extends Entity<Long>{
    private String name;
    private Integer age;
    private String passwd;

    public Participant(String name, Integer age,String passwd) {
        this.name = name;
        this.age = age;
        this.passwd = passwd;
    }

    public Participant(){}

    /**
     *  Static method, generated and ID for a user
     *  ID is Long with 4 digits
     * @return Generated ID type Long
     */
    public static Long generateID(){
        Random rand = new Random();
        return rand.longs(0,100).findFirst().getAsLong();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "Participant: " +  getName() + " " + getAge() + " " + getPasswd();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return getName().equals(that.getName()) &&
                getAge().equals(that.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge());
    }
}