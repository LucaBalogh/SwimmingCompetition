package project.model;

public enum Distanta {
    SHORT(50),MEDIUM(200),LONG(800),MARATHON(1500);
    private int value;

    private Distanta(int value) {
        this.value = value;
    }
}
