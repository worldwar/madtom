package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.google.common.collect.Lists;

import java.util.List;

public class Shift {
    private Piece wildcard;
    private List<Piece> pieces = Lists.newArrayList();

    public Shift(Piece wildcard) {
        this.wildcard = wildcard;
    }

    public Shift(Piece wildcard, List<Piece> pieces) {
        this.wildcard = wildcard;
        this.pieces = pieces;
    }

    public boolean hard() {
        return $.all(pieces, piece -> piece.equals(wildcard));
    }
}
