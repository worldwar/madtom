package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Trunk;

import java.util.List;

public class WinWaitRule implements WaitRule{
    public static final WinWaitRule instance = new WinWaitRule();

    @Override
    public boolean shouldWait(Board board, Action action) {
        Trunk player = board.trunk();
        List<Trunk> trunks = board.otherTrunks();
        return $.any(trunks, trunk -> board.winnable(trunk, player, action));
    }
}
