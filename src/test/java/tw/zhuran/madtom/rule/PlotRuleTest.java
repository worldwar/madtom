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

    @Test
    public void testPengRule() {
        trunk.init(Lists.newArrayList(Pieces.SANTIAO, Pieces.SANTIAO,
                Pieces.SITIAO, Pieces.SITIAO, Pieces.SITIAO,
                Pieces.QITIAO, Pieces.QITIAO,  Pieces.QITIAO,
                Pieces.JIUTIAO,Pieces.JIUTIAO,Pieces.JIUTIAO,
                Pieces.YITONG, Pieces.YITONG
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        trunk.feed(Pieces.YITONG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isPeng));

        trunk.discard(Pieces.YITONG);
        trunk.peng(Pieces.YITONG);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isPeng));

        trunk.discard(Pieces.SANTIAO);
        trunk.gang(Pieces.JIUTIAO);
        trunk.feed(Pieces.SANTIAO);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isPeng));

        trunk.discard(Pieces.SANTIAO);
        trunk.feed(Pieces.ERTIAO);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isPeng));

        trunk.discard(Pieces.ERTIAO);
        trunk.chi(Pieces.ERTIAO, Pieces.sequence(Pieces.ERTIAO, 1));

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isPeng));
    }
}