package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.github.underscore.Function1;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Trunk;

import java.util.List;

public class WinWaitRule implements WaitRule{
    public static final WinWaitRule instance = new WinWaitRule();

    @Override
    public boolean shouldWait(Board board, Action action) {
        return waiters(board, action).size() > 0;
    }

    @Override
    public List<Integer> waiters(final Board board, final Action action) {
        final Trunk player = board.trunk();
        List<Trunk> trunks = board.otherOrderedTrunks();
        return $.chain(trunks).filter(new Predicate<Trunk>() {
            @Override
            public Boolean apply(Trunk trunk) {
                return board.winnable(trunk, player, action);
            }
        }).map(new Function1<Trunk, Integer>() {
            @Override
            public Integer apply(Trunk trunk) {
                return trunk.player();
            }
        }).value();
    }
}
