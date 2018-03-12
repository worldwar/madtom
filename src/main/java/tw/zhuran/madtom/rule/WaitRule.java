package tw.zhuran.madtom.rule;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;

public interface WaitRule {
    boolean shouldWait(Board board, Action action);
}
