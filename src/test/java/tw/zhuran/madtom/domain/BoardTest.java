package tw.zhuran.madtom.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BoardTest {
    private Board board;

    @Before
    public void before() {
        board = new Board(4);
        board.setDealer(2);
    }

    @Test
    public void testDeal() {
        board.cut(1, 1);

        Trunk trunk = board.trunk();
        Hand hand = trunk.getHand();
        assertThat(hand.pieces().size() + hand.getHongzhongPieces().size() + hand.getWildcards().size(), is(14));
    }
}
