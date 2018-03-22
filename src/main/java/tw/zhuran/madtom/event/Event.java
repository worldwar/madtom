package tw.zhuran.madtom.event;

import tw.zhuran.madtom.domain.Action;

public class Event {
    private EventType eventType;
    private Action action;
    private int player;

    public Event(EventType eventType, int player) {
        this(eventType, null, player);
    }

    public Event(EventType eventType, Action action, int player) {
        this.eventType = eventType;
        this.action = action;
        this.player = player;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getPlayer() {
        return player;
    }

    public Event duplicate() {
        return new Event(eventType, action, player);
    }
}
