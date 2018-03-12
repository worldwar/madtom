package tw.zhuran.madtom.state;

import org.junit.Before;
import org.junit.Test;
import tw.zhuran.madtom.domain.*;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StateTest {

    private Board board;

    @Before
    public void before() {
        board = new Board(4);
    }

    @Test
    public void shouldSwitchToFreeAfterDeal() {
        board.cut(1, 1);
        assertThat(board.state(), is(BoardStateType.FREE));
    }

    @Test
    public void shouldSwitchToWaitAfterDiscard() {
        board.cut(1, 1);
        Trunk trunk = board.trunk();
        Hand hand = trunk.getHand();
        List<Piece> pieces = hand.pieces();
        Piece piece = pieces.get(0);
        board.perform(Actions.discard(piece));
        assertThat(board.state(), is(BoardStateType.WAIT));
    }
}