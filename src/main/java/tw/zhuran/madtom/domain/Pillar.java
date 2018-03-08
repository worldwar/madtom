package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;

import java.util.List;

public class Pillar {
    private Piece top;
    private Piece bottom;

    public Pillar(List<Piece> pieces) {
        top = pieces.get(0);
        bottom = pieces.get(1);
    }

    public Piece afford() {
        Piece piece;
        if (top != null) {
            piece = top;
            top = null;
        } else {
            piece = bottom;
            bottom = null;
        }
        return piece;
    }

    public boolean empty() {
        return top == null && bottom == null;
    }

    public boolean notEmpty() {
        return bottom != null;
    }

    public void suck(Pillar pillar) {
        this.top = pillar.top;
        this.bottom = pillar.bottom;
        pillar.top = null;
        pillar.bottom = null;
    }

    public Pillar copy() {
        return new Pillar(Lists.newArrayList(top, bottom));
    }
}
