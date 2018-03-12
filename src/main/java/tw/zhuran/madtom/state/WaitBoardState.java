package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;

public class WaitBoardState extends BoardState {
    public WaitBoardState(Board owner) {
        super(owner, BoardStateType.WAIT);
    }

    @Override
    public BoardStateType perform(Action action) {
        return null;
    }
}
