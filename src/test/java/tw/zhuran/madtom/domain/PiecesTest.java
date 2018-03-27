package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;
import org.junit.Test;
import tw.zhuran.madtom.util.F;

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
        assertThat(combinations.get(0), is(F.repeat(Pieces.YIWAN, 2)));
        assertThat(combinations.get(1), is(pool));
        assertThat(combinations.get(2), is(F.repeat(Pieces.ERWAN, 2)));
    }

    @Test
    public void testPermutations() throws Exception {
        List<Piece> pool = Lists.newArrayList(Pieces.YIWAN, Pieces.ERWAN);
        List<List<Piece>> permutations = Pieces.permutations(1, pool);
        assertThat(permutations.size(), is(2));

        permutations = Pieces.permutations(2, pool);
        assertThat(permutations.size(), is(4));
        assertThat(permutations.get(0), is(F.repeat(Pieces.YIWAN, 2)));
        assertThat(permutations.get(1), is(pool));
        List<Piece> value = Lists.newArrayList(Pieces.ERWAN, Pieces.YIWAN);
        assertThat(permutations.get(2), is(value));
        assertThat(permutations.get(3), is(F.repeat(Pieces.ERWAN, 2)));
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
