package tw.zhuran.madtom.rule;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;

public class ChiWaitRule implements WaitRule {
    public static final ChiWaitRule instance = new ChiWaitRule();

    @Override
    public boolean shouldWait(Board board, Action action) {
        return board.nextTrunk().chiable(action.getPiece());
    }
}
