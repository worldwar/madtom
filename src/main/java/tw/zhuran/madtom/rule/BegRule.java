package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.Plot;

public class BegRule implements PlotRule {
    public static final BegRule instance = new BegRule();

    @Override
    public void apply(Plot plot) {
        boolean self = plot.isSelf();
        boolean onlyOnePairInHand = plot.getForm().getGroups().size() == 1;
        boolean hasAngang = $.any(plot.getActions(), action -> action.getGroup() != null && action.getType() == ActionType.ANGANG);
        boolean beg = (!self) && onlyOnePairInHand && (!hasAngang);
        plot.setBeg(beg);
    }
}
