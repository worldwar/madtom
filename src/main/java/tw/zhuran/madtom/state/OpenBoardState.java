package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;

public class OpenBoardState extends BoardState {
    public OpenBoardState(Board owner) {
        super(owner, BoardStateType.OPEN);
    }

    @Override
    public BoardStateType perform(Event event) {
        Action action = event.getAction();
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

    @Override
    public boolean instant() {
        return false;
    }
}
