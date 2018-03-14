package tw.zhuran.madtom.rule;

import com.google.common.collect.Lists;

import java.util.List;

public class Rules {
    private static List<WaitRule> waitRules = Lists.newArrayList();
    private static List<WaitRule> xugangWaitRules = Lists.newArrayList();

    static {
        waitRules.add(WinWaitRule.instance);
        waitRules.add(GangWaitRule.instance);
        waitRules.add(PengWaitRule.instance);
        waitRules.add(ChiWaitRule.instance);

        xugangWaitRules.add(WinWaitRule.instance);
    }

    public static List<WaitRule> discardWaitRules() {
        return waitRules;
    }

    public static List<WaitRule> xugangWaitRules() {
        return xugangWaitRules;
    }
}
