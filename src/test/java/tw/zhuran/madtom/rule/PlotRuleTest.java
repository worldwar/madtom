package tw.zhuran.madtom.rule;

import com.github.underscore.$;
import com.github.underscore.Predicate;
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
                Pieces.QITIAO, Pieces.QITIAO, Pieces.QITIAO));
        trunk.setWildcard(Pieces.ERTIAO);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isHard();
            }
        }));

        trunk.discard(Pieces.SANTIAO);
        trunk.feed(Pieces.ERTIAO);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isHard();
            }
        }));
        assertFalse($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isHard();
            }
        }));

        trunk.discard(Pieces.SANTIAO);
        trunk.feed(Pieces.YITIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isHard();
            }
        }));
    }

    @Test
    public void testPengRule() {
        trunk.init(Lists.newArrayList(Pieces.SANTIAO, Pieces.SANTIAO,
                Pieces.SITIAO, Pieces.SITIAO, Pieces.SITIAO,
                Pieces.QITIAO, Pieces.QITIAO, Pieces.QITIAO,
                Pieces.JIUTIAO, Pieces.JIUTIAO, Pieces.JIUTIAO,
                Pieces.YITONG, Pieces.YITONG
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        trunk.feed(Pieces.YITONG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isPeng();
            }
        }));

        trunk.discard(Pieces.YITONG);
        trunk.peng(Pieces.YITONG);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isPeng();
            }
        }));

        trunk.discard(Pieces.SANTIAO);
        trunk.gang(Pieces.JIUTIAO);
        trunk.feed(Pieces.SANTIAO);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isPeng();
            }
        }));

        trunk.discard(Pieces.SANTIAO);
        trunk.feed(Pieces.ERTIAO);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isPeng();
            }
        }));

        trunk.discard(Pieces.ERTIAO);
        trunk.chi(Pieces.ERTIAO, Pieces.sequence(Pieces.ERTIAO, 1));

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isPeng();
            }
        }));
    }

    @Test
    public void testSuitRule() {
        trunk.init(Lists.newArrayList(Pieces.SANTIAO, Pieces.SANTIAO,
                Pieces.SITIAO, Pieces.SITIAO, Pieces.SITIAO,
                Pieces.QITIAO, Pieces.QITIAO, Pieces.QITIAO,
                Pieces.BATIAO, Pieces.BATIAO, Pieces.BATIAO,
                Pieces.JIUTIAO, Pieces.JIUTIAO, Pieces.JIUTIAO
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));

        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.DONGFENG);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));

        trunk.laiziGang();
        trunk.feed(Pieces.JIUTIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));

        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.YIWAN);
        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.YIWAN);
        trunk.discard(Pieces.JIUTIAO);
        trunk.feed(Pieces.DONGFENG);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));

        trunk.laiziGang();
        trunk.feed(Pieces.SANTIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));

        trunk.discard(Pieces.SITIAO);
        trunk.peng(Pieces.SITIAO);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));

        trunk.discard(Pieces.QITIAO);
        trunk.peng(Pieces.YIWAN);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isSuit();
            }
        }));
    }

    @Test
    public void testFengRule() {
        trunk.init(Lists.newArrayList(Pieces.DONGFENG, Pieces.DONGFENG,
                Pieces.NANFENG, Pieces.NANFENG, Pieces.NANFENG,
                Pieces.XIFENG, Pieces.XIFENG, Pieces.XIFENG,
                Pieces.BEIFENG, Pieces.BEIFENG, Pieces.BEIFENG,
                Pieces.YIWAN, Pieces.YIWAN, Pieces.YIWAN
        ));
        trunk.setWildcard(Pieces.WUTONG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isFeng();
            }
        }));

        trunk.discard(Pieces.YIWAN);
        trunk.peng(Pieces.DONGFENG);
        trunk.discard(Pieces.YIWAN);
        trunk.peng(Pieces.NANFENG);
        trunk.discard(Pieces.YIWAN);
        trunk.feed(Pieces.NANFENG);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isFeng();
            }
        }));

        trunk.discard(Pieces.NANFENG);
        trunk.feed(Pieces.ERWAN);
        trunk.discard(Pieces.NANFENG);
        trunk.feed(Pieces.ERWAN);
        trunk.discard(Pieces.XIFENG);
        trunk.peng(Pieces.ERWAN);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isFeng();
            }
        }));
    }

    @Test
    public void testJiangRule() {
        trunk.init(Lists.newArrayList(Pieces.ERWAN, Pieces.ERWAN,
                Pieces.WUWAN, Pieces.WUWAN, Pieces.WUWAN,
                Pieces.ERTIAO, Pieces.ERTIAO, Pieces.ERTIAO,
                Pieces.BATIAO, Pieces.BATIAO, Pieces.BATIAO,
                Pieces.ERTONG, Pieces.ERTONG, Pieces.ERTONG
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isJiang();
            }
        }));

        trunk.discard(Pieces.ERTONG);
        trunk.peng(Pieces.ERWAN);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isJiang();
            }
        }));

        trunk.discard(Pieces.ERTONG);
        trunk.feed(Pieces.SANTONG);
        trunk.discard(Pieces.ERTONG);
        trunk.feed(Pieces.SANTONG);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isJiang();
            }
        }));

        trunk.discard(Pieces.WUWAN);
        trunk.peng(Pieces.SANTONG);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isJiang();
            }
        }));
    }

    @Test
    public void testBegRuleWithoutAngang() {
        trunk.init(Lists.newArrayList(Pieces.ERWAN, Pieces.ERWAN,
                Pieces.WUWAN, Pieces.WUWAN, Pieces.WUWAN,
                Pieces.ERTIAO, Pieces.ERTIAO, Pieces.ERTIAO,
                Pieces.BATIAO, Pieces.BATIAO, Pieces.BATIAO,
                Pieces.ERTONG, Pieces.ERTONG, Pieces.ERTONG
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isBeg();
            }
        }));

        trunk.discard(Pieces.WUWAN);
        trunk.peng(Pieces.ERWAN);
        trunk.discard(Pieces.ERTONG);
        trunk.feed(Pieces.ERTONG);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isBeg();
            }
        }));

        trunk.discard(Pieces.ERTONG);
        trunk.gang(Pieces.ERTIAO);
        trunk.feed(Pieces.XIFENG);
        trunk.discard(Pieces.XIFENG);
        trunk.gang(Pieces.BATIAO);
        trunk.feed(Pieces.XIFENG);
        trunk.discard(Pieces.XIFENG);
        trunk.peng(Pieces.ERTONG);
        trunk.discard(Pieces.WUWAN);
        trunk.feed(Pieces.WUWAN);

        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertTrue($.all(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isBeg();
            }
        }));
    }

    @Test
    public void testBegRuleWithAngang() {
        trunk.init(Lists.newArrayList(Pieces.ERWAN, Pieces.ERWAN,
                Pieces.WUWAN, Pieces.WUWAN, Pieces.WUWAN,
                Pieces.ERTIAO, Pieces.ERTIAO, Pieces.ERTIAO,
                Pieces.BATIAO, Pieces.BATIAO, Pieces.BATIAO,
                Pieces.ERTONG, Pieces.ERTONG, Pieces.ERTONG
        ));
        trunk.setWildcard(Pieces.DONGFENG);
        List<Plot> plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isBeg();
            }
        }));

        trunk.discard(Pieces.ERWAN);
        trunk.feed(Pieces.WUWAN);
        trunk.angang(Pieces.WUWAN);
        trunk.feed(Pieces.ERTIAO);
        trunk.angang(Pieces.ERTIAO);
        trunk.feed(Pieces.FACAI);
        trunk.discard(Pieces.FACAI);
        trunk.peng(Pieces.ERTONG);
        trunk.discard(Pieces.ERTONG);
        trunk.feed(Pieces.ERWAN);
        trunk.discard(Pieces.ERWAN);
        trunk.peng(Pieces.BATIAO);
        trunk.discard(Pieces.BATIAO);
        trunk.feed(Pieces.ERWAN);
        plots = trunk.plots();
        assertTrue(plots.size() > 0);
        assertFalse($.any(plots, new Predicate<Plot>() {
            @Override
            public Boolean apply(Plot plot) {
                return plot.isBeg();
            }
        }));
    }
}