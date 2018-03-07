package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.GroupType;
import tw.zhuran.madtom.domain.Plot;

public class PengRule implements PlotRule {
    public static final PengRule instance = new PengRule();

    @Override
    public void apply(Plot plot) {
        boolean hasChi = $.any(plot.getActions(), action ->
                action.getType() == ActionType.CHI);
        boolean hasSequence = $.any(plot.getForm().getGroups(), group -> group.getGroupType() == GroupType.SEQUENCE);
        boolean isPeng = !(hasChi || hasSequence);
        plot.setPeng(isPeng);
    }
}
