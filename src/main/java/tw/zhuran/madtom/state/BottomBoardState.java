package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.event.Event;

public class BottomBoardState extends BoardState{
    public BottomBoardState(Board owner) {
        super(owner, BoardStateType.BOTTOM);
    }

    @Override
    public BoardStateType perform(Event event) {
        while (owner.bottom()) {
            owner.turnNext();
            owner.dispatch();
            if (owner.currentWinnable()) {
                owner.settle();
                return BoardStateType.CLOSE;
            }
        }
        owner.draw();
        return BoardStateType.CLOSE;
    }

    @Override
    public boolean instant() {
        return true;
    }
}
