package tw.zhuran.madtom.util;

import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.state.*;

import java.util.List;

public class States {
    public static List<BoardState> states(Board board) {
        List<BoardState> states = Lists.newArrayList();
        states.add(new FreeBoardState(board));
        states.add(new OpenBoardState(board));
        states.add(new WaitBoardState(board));
        states.add(new DispatchBoardState(board));
        states.add(new BottomBoardState(board));
        states.add(new CloseBoardState(board));
        return states;
    }
}
