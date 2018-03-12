package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Trunk;

import java.util.List;

public class PengWaitRule implements WaitRule {
    public static final PengWaitRule instance = new PengWaitRule();

    @Override
    public boolean shouldWait(Board board, Action action) {
        List<Trunk> trunks = board.otherTrunks();
        Piece piece = action.getPiece();
        return $.any(trunks, trunk -> trunk.pengable(piece) || trunk.gangable(piece));
    }
}
