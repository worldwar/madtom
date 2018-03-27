package tw.zhuran.madtom.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TurnerTest {

    @Test
    public void testNaturalTurner() {
        NaturalTurner turner = new NaturalTurner(8);
        assertThat(turner.current(), is(1));
        assertThat(turner.next(), is(2));
        assertThat(turner.previous(), is(8));

        turner.turnNext();

        assertThat(turner.current(), is(2));
        assertThat(turner.next(), is(3));
        assertThat(turner.previous(), is(1));

        turner.turnTo(5);

        assertThat(turner.current(), is(5));
        assertThat(turner.next(), is(6));
        assertThat(turner.previous(), is(4));

        turner.turnTo(8);

        assertThat(turner.current(), is(8));
        assertThat(turner.next(), is(1));
        assertThat(turner.previous(), is(7));
    }

    @Test
    public void testReverseNaturalTurner() {
        ReverseNaturalTurner turner = new ReverseNaturalTurner(5);
        assertThat(turner.current(), is(1));
        assertThat(turner.next(), is(5));
        assertThat(turner.previous(), is(2));

        turner.turnNext();

        assertThat(turner.current(), is(5));
        assertThat(turner.next(), is(4));
        assertThat(turner.previous(), is(1));

        turner.turnTo(3);

        assertThat(turner.current(), is(3));
        assertThat(turner.next(), is(2));
        assertThat(turner.previous(), is(4));

        turner.turnTo(5);

        assertThat(turner.current(), is(5));
        assertThat(turner.next(), is(4));
        assertThat(turner.previous(), is(1));
    }
}