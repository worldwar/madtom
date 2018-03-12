package tw.zhuran.madtom.util;

import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.state.BoardState;
import tw.zhuran.madtom.state.FreeBoardState;
import tw.zhuran.madtom.state.WaitBoardState;

import java.util.List;

public class States {
    public static List<BoardState> states(Board board) {
        List<BoardState> states = Lists.newArrayList();
        states.add(new FreeBoardState(board));
        states.add(new WaitBoardState(board));
        return states;
    }
}
