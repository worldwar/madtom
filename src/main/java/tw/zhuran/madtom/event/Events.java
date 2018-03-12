package tw.zhuran.madtom.event;

import tw.zhuran.madtom.domain.Action;

public class Events {
    public static Event win(int player) {
        return new Event(EventType.WIN, null, player);
    }
    public static Event pass(int player) {
        return new Event(EventType.PASS, null, player);
    }
    public static Event action(int player, Action action) {
        return new Event(EventType.ACTION, action, player);
    }

}
