package tw.zhuran.madtom.domain;

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
}
