package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Result;
import tw.zhuran.madtom.event.Event;

public class CloseBoardState extends BoardState{
    public CloseBoardState(Board owner) {
        super(owner, BoardStateType.CLOSE);
    }

    @Override
    public BoardStateType perform(Event action) {
        return BoardStateType.CLOSE;
    }

    @Override
    public String toString() {
        Result result = owner.getResult();
        if (result == null) {
            return "结果未计算";
        }
        return result.toString();
    }
}
