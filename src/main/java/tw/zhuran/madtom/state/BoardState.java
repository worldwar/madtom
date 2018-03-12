package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.event.Event;

public abstract class BoardState extends AbstractState<Board, Event, BoardStateType> {
    public BoardState(Board owner, BoardStateType type) {
        super(owner, type);
    }
}
