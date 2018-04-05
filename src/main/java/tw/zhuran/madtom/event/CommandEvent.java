package tw.zhuran.madtom.event;

public class CommandEvent extends InterceptEvent {
    public CommandEvent(int player) {
        super(player);
        eventType = EventType.COMMAND;
    }
}
