package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;

public class FreeBoardState extends BoardState {
    public FreeBoardState(Board owner) {
        super(owner, BoardStateType.FREE);
    }

    @Override
    public BoardStateType perform(Event event) {
        Action action = event.getAction();
        if (event.getEventType() == EventType.WIN) {
            owner.winnable(event.getPlayer());
            return BoardStateType.CLOSE;
        }
        if (action == null) {
            return this.type();
        }
        ActionType type = action.getType();
        if (Actions.free(type)) {
            owner.execute(action);
            if (owner.shouldWait(action)) {
                return BoardStateType.WAIT;
            } else {
                return BoardStateType.DISPATCH;
            }
        }
        return this.type;
    }
}
