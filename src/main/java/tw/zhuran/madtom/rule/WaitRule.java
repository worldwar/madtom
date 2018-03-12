package tw.zhuran.madtom.rule;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;

import java.util.List;

public interface WaitRule {
    boolean shouldWait(Board board, Action action);
    List<Integer> waiters(Board board, Action action);
}
