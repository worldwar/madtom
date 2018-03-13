package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;

public class OpenBoardState extends FreeBoardState {
    public OpenBoardState(Board owner) {
        super(owner, BoardStateType.OPEN);
    }

    @Override
    public BoardStateType perform(Event event) {
        if (event.getEventType() == EventType.WIN) {
            return this.type;
        }
        return super.perform(event);
    }

    @Override
    public boolean instant() {
        return false;
    }
}
