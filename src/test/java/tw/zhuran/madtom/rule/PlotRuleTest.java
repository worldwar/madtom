package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import tw.zhuran.madtom.domain.Hand;
import tw.zhuran.madtom.domain.Pieces;
import tw.zhuran.madtom.domain.Plot;
import tw.zhuran.madtom.domain.Trunk;

import java.util.List;

import static org.junit.Assert.*;
public class PlotRuleTest {

    private Trunk trunk;
    @Before
    public void before() {
        trunk = new Trunk(new Hand());
    }

    @Test
    public void testHardRule() {
        trunk.init(Lists.newArrayList(Pieces.SANTIAO, Pieces.SANTIAO,
                Pieces.SITIAO, Pieces.SITIAO, Pieces.SITIAO,
                Pieces.WUTIAO, Pieces.WUTIAO, Pieces.WUTIAO,
                Pieces.LIUTIAO, Pieces.LIUTIAO, Pieces.LIUTIAO,
                Pieces.QITIAO, Pieces.QITIAO,  Pieces.QITIAO));
        trunk.setWildcard(Pieces.DONGFENG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isHard));
    }
}