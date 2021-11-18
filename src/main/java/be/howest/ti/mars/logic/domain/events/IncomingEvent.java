package be.howest.ti.mars.logic.domain.events;

public abstract class IncomingEvent extends Event{

    private String marsid;
    public IncomingEvent(EventType type, String marsid) {
        super(type);
        this.marsid = marsid;
    }

    public String getMarsid() {
        return marsid;
    }
}
