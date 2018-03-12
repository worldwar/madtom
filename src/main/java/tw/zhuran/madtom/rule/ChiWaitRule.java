package tw.zhuran.madtom.rule;

import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Trunk;

import java.util.List;

public class ChiWaitRule implements WaitRule {
    public static final ChiWaitRule instance = new ChiWaitRule();

    @Override
    public boolean shouldWait(Board board, Action action) {
        return waiters(board, action).size() > 0;
    }

    @Override
    public List<Integer> waiters(Board board, Action action) {
        Trunk trunk = board.nextTrunk();
        if (trunk.chiable(action.getPiece())) {
            return Lists.newArrayList(trunk.player());
        } else {
            return Lists.newArrayList();
        }
    }
}
