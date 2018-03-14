package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.util.States;

public class BoardStateManager extends StateManager<Event, BoardStateType, BoardState> {
    public BoardStateManager(Board owner) {
        super(States.states(owner));
    }

    public Piece waitPiece() {
        if (currentState() == BoardStateType.WAIT) {
            WaitBoardState state = (WaitBoardState)state(currentState());
            return state.waitPiece();
        }
        return null;
    }
}
