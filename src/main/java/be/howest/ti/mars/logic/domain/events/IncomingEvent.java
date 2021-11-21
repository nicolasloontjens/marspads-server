package be.howest.ti.mars.logic.domain.events;

public abstract class IncomingEvent extends Event{

    private int marsid;
    public IncomingEvent(EventType type, int marsid) {
        super(type);
        this.marsid = marsid;
    }

    public int getMarsid() {
        return marsid;
    }
}
