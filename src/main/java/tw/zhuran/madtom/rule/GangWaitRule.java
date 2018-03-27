package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.github.underscore.Function1;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Trunk;

import java.util.List;

public class GangWaitRule implements WaitRule {
    public static final GangWaitRule instance = new GangWaitRule();

    @Override
    public boolean shouldWait(Board board, Action action) {
        return waiters(board, action).size() > 0;
    }

    @Override
    public List<Integer> waiters(Board board, Action action) {
        List<Trunk> trunks = board.otherTrunks();
        final Piece piece = action.getPiece();
        return $.chain(trunks).filter(new Predicate<Trunk>() {
            @Override
            public Boolean apply(Trunk trunk) {
                return trunk.gangable(piece);
            }
        }).map(new Function1<Trunk, Integer>() {
            @Override
            public Integer apply(Trunk trunk) {
                return trunk.player();
            }
        }).value();
    }
}
