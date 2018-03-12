package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.util.States;

public class BoardStateManager extends StateManager<Event, BoardStateType, BoardState> {
    public BoardStateManager(Board owner) {
        super(States.states(owner));
    }
}
