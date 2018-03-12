package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;

public abstract class BoardState extends AbstractState<Board, Action, BoardStateType> {
    public BoardState(Board owner, BoardStateType type) {
        super(owner, type);
    }
}
