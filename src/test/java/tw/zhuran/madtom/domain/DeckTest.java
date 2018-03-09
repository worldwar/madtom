package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DeckTest {

    private Deck deck = new Deck(4);

    @Test
    public void testGangAfford() throws Exception {
        deck.cut(1, 1);

        deck.gangAfford(2);

        Wall wall = deck.wall(1);

        assertThat(wall.tailLength(), is(1));

        deck.gangAfford(2);

        assertThat(wall.tailLength(), is(0));
    }

    @Test
    public void testDeal() {
        deck.cut(2, 16);
        Wall startWall = deck.wall(2);
        Wall nextWall = deck.wall(1);

        deck.deal();
        assertFalse(startWall.affordable());
        assertTrue(nextWall.affordable());

        $.times(4, () -> deck.deal());
        assertFalse(nextWall.affordable());
    }
}
