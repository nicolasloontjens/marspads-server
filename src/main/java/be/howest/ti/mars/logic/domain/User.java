package be.howest.ti.mars.logic.domain;

public class User {
    private final String name;
    private final int marsid;
    private int contactid;

    public User(int marsid, String name, int contactid){
        this.name = name;
        this.marsid = marsid;
        this.contactid = contactid;
    }

    public User(int marsid, String name){
        this.name = name;
        this.marsid = marsid;
        this.contactid = 0;
    }

    public String getName() {
        return name;
    }

    public int getMarsid() {
        return marsid;
    }

    public int getContactid() {
        return contactid;
    }

    public void setContactid(int contactid) {
        this.contactid = contactid;
    }
}
