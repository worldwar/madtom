package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.github.underscore.Function1;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.Group;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Pieces;
import tw.zhuran.madtom.domain.Plot;

import java.util.List;

public class JiangRule implements PlotRule {
    public static final JiangRule instance = new JiangRule();

    @Override
    public void apply(Plot plot) {
        List<Piece> pieces = $.chain(plot.allGroups()).map(new Function1<Group, Object>() {
            @Override
            public Object apply(Group group) {
                return group.getPieces();
            }
        }).flatten().value();
        boolean jiang = $.all(pieces, new Predicate<Piece>() {
            @Override
            public Boolean apply(Piece piece) {
                return Pieces.isJiang(piece);
            }
        });
        plot.setJiang(jiang);
    }
}
