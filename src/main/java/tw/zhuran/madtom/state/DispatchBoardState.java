package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.event.Event;

public class DispatchBoardState extends BoardState {
    public DispatchBoardState(Board owner) {
        super(owner, BoardStateType.DISPATCH);
    }

    @Override
    public BoardStateType perform(Event action) {
        if (owner.bottom()) {
            return BoardStateType.BOTTOM;
        }
        owner.turnNext();
        owner.dispatch();
        return BoardStateType.FREE;
    }

    @Override
    public boolean instant() {
        return true;
    }
}
