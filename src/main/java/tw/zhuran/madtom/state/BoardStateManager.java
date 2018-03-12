package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.util.States;

public class BoardStateManager extends StateManager<Action, BoardStateType, BoardState> {
    public BoardStateManager(Board owner) {
        super(States.states(owner));
    }
}
