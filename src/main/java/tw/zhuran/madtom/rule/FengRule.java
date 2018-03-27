package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.Group;
import tw.zhuran.madtom.domain.Kind;
import tw.zhuran.madtom.domain.Plot;

public class FengRule implements PlotRule {
    public static final FengRule instance = new FengRule();

    @Override
    public void apply(Plot plot) {
        boolean feng = $.all(plot.allGroups(), new Predicate<Group>() {
            @Override
            public Boolean apply(Group group) {
                return group.getKind() == Kind.FENG;
            }
        });
        plot.setFeng(feng);
    }
}
