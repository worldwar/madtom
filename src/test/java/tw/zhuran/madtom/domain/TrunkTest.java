package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TrunkTest {
    private Trunk trunk;

    @Before
    public void before() {
        trunk = new Trunk(Lists.newArrayList(
                Pieces.YIWAN, Pieces.ERWAN, Pieces.ERWAN,
                Pieces.ERWAN, Pieces.SANWAN, Pieces.DONGFENG,
                Pieces.SITIAO, Pieces.XIFENG, Pieces.WUTONG,
                Pieces.BEIFENG, Pieces.BAWAN, Pieces.BATIAO,
                Pieces.BATIAO
        ));
    }

    @Test
    public void testDiscard() throws Exception {
        trunk.feed(Pieces.JIUWAN);
        trunk.discard(Pieces.DONGFENG);
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(actions.size(), is(1));
        assertThat(action.getPiece(), is(Pieces.DONGFENG));
        assertThat(action.getType(), is(ActionType.DISCARD));
        assertThat(hand.getFengPieces().size(), is(2));
    }

    @Test
    public void testChi() throws Exception {
        trunk.chi(Pieces.SANWAN, Pieces.sequence(Pieces.SANWAN, 3));
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.CHI));
        assertThat(action.getGroup().getGroupType(), is(GroupType.SEQUENCE));
        assertThat(hand.getWanPieces().size(), is(4));
        assertThat(hand.getWanPieces().get(0), is(Pieces.ERWAN));
    }

    @Test
    public void testPeng() throws Exception {
        trunk.peng(Pieces.ERWAN);
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.PENG));
        assertThat(action.getGroup().getGroupType(), is(GroupType.TRIPLE));
        assertThat(hand.getWanPieces().size(), is(4));
        assertThat(hand.getWanPieces().get(0), is(Pieces.YIWAN));
    }

    @Test
    public void testGang() throws Exception {
        trunk.gang(Pieces.ERWAN);
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.GANG));
        assertThat(action.getGroup().getGroupType(), is(GroupType.TRIPLE));
        assertThat(hand.getWanPieces().size(), is(3));
        assertThat(hand.getWanPieces().get(1), is(Pieces.SANWAN));
    }

    @Test
    public void testXugang() throws Exception {
        trunk.peng(Pieces.BATIAO);
        trunk.discard(Pieces.WUTONG);
        trunk.feed(Pieces.BATIAO);
        trunk.xugang(Pieces.BATIAO);
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.XUGANG));
        assertThat(action.getGroup().getGroupType(), is(GroupType.TRIPLE));
        assertThat(hand.getTiaoPieces().size(), is(1));
    }

    @Test
    public void testAngang() throws Exception {
        trunk.feed(Pieces.ERWAN);
        trunk.angang(Pieces.ERWAN);
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.ANGANG));
        assertThat(action.getGroup().getGroupType(), is(GroupType.TRIPLE));
        assertThat(hand.getWanPieces().size(), is(3));
    }

    @Test
    public void testHongzhongGang() throws Exception {
        trunk.feed(Pieces.HONGZHONG);
        trunk.hongzhongGang();
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.HONGZHONG_GANG));
        assertNull(action.getGroup());
        assertThat(action.getPiece(), is(Pieces.HONGZHONG));
        assertThat(hand.getHongzhongPieces().size(), is(0));
    }

    @Test
    public void testLaiziGang() throws Exception {
        trunk.setWildcard(Pieces.BAIBAN);
        trunk.feed(Pieces.BAIBAN);
        trunk.laiziGang();
        Hand hand = trunk.getHand();
        List<Action> actions = trunk.getActions();
        Action action = actions.get(0);
        assertThat(action.getType(), is(ActionType.LAIZI_GANG));
        assertNull(action.getGroup());
        assertThat(action.getPiece(), is(Pieces.BAIBAN));
        assertThat(hand.getWildcards().size(), is(0));
    }

    @Test
    public void testXugangable() {
        trunk.chi(Pieces.SANWAN, Pieces.sequence(Pieces.SANWAN, 3));
        assertFalse(trunk.xugangable());
        trunk.discard(Pieces.ERWAN);
        trunk.peng(Pieces.BATIAO);
        trunk.discard(Pieces.BAWAN);
        trunk.feed(Pieces.BATIAO);
        assertTrue(trunk.xugangable());

        List<Piece> pieces = trunk.xugangablePieces();
        assertThat(pieces.size(), is(1));
        assertThat(pieces.get(0), is(Pieces.BATIAO));

        trunk.discard(Pieces.BEIFENG);
        trunk.feed(Pieces.SITIAO);
        trunk.discard(Pieces.XIFENG);
        trunk.peng(Pieces.SITIAO);
        trunk.discard(Pieces.WUTONG);
        trunk.feed(Pieces.SITIAO);
        assertTrue(trunk.xugangable());
        pieces = trunk.xugangablePieces();
        assertThat(pieces.size(), is(2));
        assertTrue(pieces.contains(Pieces.BATIAO));
        assertTrue(pieces.contains(Pieces.SITIAO));

        trunk.discard(Pieces.BATIAO);
        trunk.feed(Pieces.JIUTONG);
        assertTrue(trunk.xugangable());
        pieces = trunk.xugangablePieces();
        assertThat(pieces.size(), is(1));
        assertTrue(pieces.contains(Pieces.SITIAO));

        trunk.xugang(Pieces.SITIAO);
        trunk.feed(Pieces.JIUTONG);
        assertFalse(trunk.xugangable());
    }
}
