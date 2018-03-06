package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PiecesTest {
    @Test
    public void testCombinations() throws Exception {
        List<Piece> pool = Lists.newArrayList(Pieces.YIWAN, Pieces.ERWAN);
        List<List<Piece>> combinations = Pieces.combinations(1, pool);
        assertThat(combinations.size(), is(2));

        combinations = Pieces.combinations(2, pool);
        assertThat(combinations.size(), is(3));
        assertThat(combinations.get(0), is(Pieces.repeat(Pieces.YIWAN, 2)));
        assertThat(combinations.get(1), is(pool));
        assertThat(combinations.get(2), is(Pieces.repeat(Pieces.ERWAN, 2)));
    }

    @Test
    public void testPermunations() throws Exception {
        List<Piece> pool = Lists.newArrayList(Pieces.YIWAN, Pieces.ERWAN);
        List<List<Piece>> permunations = Pieces.permutations(1, pool);
        assertThat(permunations.size(), is(2));

        permunations = Pieces.permutations(2, pool);
        assertThat(permunations.size(), is(4));
        assertThat(permunations.get(0), is(Pieces.repeat(Pieces.YIWAN, 2)));
        assertThat(permunations.get(1), is(pool));
        assertThat(permunations.get(2), is(Lists.newArrayList(Pieces.ERWAN, Pieces.YIWAN)));
        assertThat(permunations.get(3), is(Pieces.repeat(Pieces.ERWAN, 2)));
    }

    @Test
    public void testUniqueCombinations() throws Exception {
        List<Piece> pool = Lists.newArrayList(Pieces.YIWAN, Pieces.YIWAN, Pieces.YIWAN, Pieces.ERWAN, Pieces.ERWAN, Pieces.ERWAN);
        List<List<Piece>> uc = Pieces.uniqueCombinations(1, pool);
        assertThat(uc.size(), is(2));

        uc = Pieces.uniqueCombinations(2, pool);
        assertThat(uc.size(), is(3));
        uc = Pieces.uniqueCombinations(3, pool);
        assertThat(uc.size(), is(4));
    }
}
