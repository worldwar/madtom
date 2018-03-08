package tw.zhuran.madtom.rule;

import tw.zhuran.madtom.domain.Pieces;
import tw.zhuran.madtom.domain.Plot;

public class SuitRule implements PlotRule {
    public static final SuitRule instance = new SuitRule();

    @Override
    public void apply(Plot plot) {
        boolean suit = Pieces.sameKind(plot.allGroups());
        plot.setSuit(suit);
    }
}
