package tw.zhuran.madtom.rule;

import tw.zhuran.madtom.domain.Plot;

public class HardRule implements PlotRule {
    public static final HardRule instance = new HardRule();

    @Override
    public void apply(Plot plot) {
        boolean hard = plot.getForm().getShift().hard();
        plot.setHard(hard);
    }
}
