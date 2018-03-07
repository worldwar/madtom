package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Group;
import tw.zhuran.madtom.domain.Pieces;
import tw.zhuran.madtom.domain.Plot;

import java.util.List;

public class SuitRule implements PlotRule {
    public static final SuitRule instance = new SuitRule();

    @Override
    public void apply(Plot plot) {
        List<Group> handGroups = plot.getForm().getGroups();
        List<Group> actionGroups = $.chain(plot.getActions()).filter(Pieces::hasGroup).map(Action::getGroup).value();
        List<Group> groups = Pieces.add(handGroups, actionGroups);
        boolean suit = Pieces.sameKind(groups);
        plot.setSuit(suit);
    }
}
