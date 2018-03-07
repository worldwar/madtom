package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import tw.zhuran.madtom.domain.*;

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
        trunk.setWildcard(Pieces.ERTIAO);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isHard));

        trunk.discard(Pieces.SANTIAO);
        trunk.feed(Pieces.ERTIAO);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.any(plots, Plot::isHard));
        assertFalse($.all(plots, Plot::isHard));

        trunk.discard(Pieces.SANTIAO);
        trunk.feed(Pieces.YITIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isHard));
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

    @Test
    public void testSuitRule() {
        trunk.init(Lists.newArrayList(Pieces.SANTIAO, Pieces.SANTIAO,
                Pieces.SITIAO, Pieces.SITIAO, Pieces.SITIAO,
                Pieces.QITIAO, Pieces.QITIAO, Pieces.QITIAO,
                Pieces.BATIAO, Pieces.BATIAO, Pieces.BATIAO,
                Pieces.JIUTIAO,Pieces.JIUTIAO,Pieces.JIUTIAO
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isSuit));

        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.DONGFENG);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isSuit));

        trunk.laiziGang();
        trunk.feed(Pieces.JIUTIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, Plot::isSuit));

        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.YIWAN);
        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.YIWAN);
        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.DONGFENG);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isSuit));

        trunk.laiziGang();
        trunk.feed(Pieces.SANTIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isSuit));

        trunk.discard(Pieces.SITIAO);
        trunk.peng(Pieces.SITIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isSuit));

        trunk.discard(Pieces.QITIAO);
        trunk.peng(Pieces.YIWAN);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, Plot::isSuit));
    }
}