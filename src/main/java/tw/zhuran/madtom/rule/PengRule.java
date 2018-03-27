package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.*;

public class PengRule implements PlotRule {
    public static final PengRule instance = new PengRule();

    @Override
    public void apply(Plot plot) {
        boolean hasChi = $.any(plot.getActions(), new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return action.getType() == ActionType.CHI;
            }
        });
        boolean hasSequence = $.any(plot.getForm().getGroups(), new Predicate<Group>() {
            @Override
            public Boolean apply(Group group) {
                return group.getGroupType() == GroupType.SEQUENCE;
            }
        });
        boolean isPeng = !(hasChi || hasSequence);
        plot.setPeng(isPeng);
    }
}
