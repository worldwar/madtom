package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Pieces;
import tw.zhuran.madtom.domain.Plot;

import java.util.List;

public class JiangRule implements PlotRule {
    public static final JiangRule instance = new JiangRule();

    @Override
    public void apply(Plot plot) {
        List<Piece> pieces = $.chain(plot.allGroups()).map(group -> group.getPieces()).flatten().value();
        boolean jiang = $.all(pieces, Pieces::isJiang);
        plot.setJiang(jiang);
    }
}
