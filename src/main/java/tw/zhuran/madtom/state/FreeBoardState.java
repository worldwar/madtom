package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;

public class FreeBoardState extends BoardState {
    public FreeBoardState(Board owner) {
        super(owner, BoardStateType.FREE);
    }

    public FreeBoardState(Board owner, BoardStateType type) {
        super(owner, type);
    }

    @Override
    public BoardStateType perform(Event event) {
        if (event.getPlayer() != owner.turn()) {
            return this.type;
        }
        Action action = event.getAction();
        if (event.getEventType() == EventType.WIN) {
            if (owner.currentWinnable()) {
                owner.settle();
                return BoardStateType.CLOSE;
            }
            return this.type;
        }
        if (action == null) {
            return this.type();
        }
        ActionType type = action.getType();
        if (Actions.free(type)) {
            owner.execute(action);
            if (Actions.genericGang(type) && type != ActionType.XUGANG) {
                owner.gangAfford();
                return BoardStateType.FREE;
            }
            if (owner.shouldWait(action)) {
                return BoardStateType.WAIT;
            } else {
                if (type == ActionType.XUGANG) {
                    owner.gangAfford();
                    return BoardStateType.FREE;
                }
                return BoardStateType.DISPATCH;
            }
        }
        return this.type;
    }
}
