package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.google.common.collect.Lists;

import java.util.List;

public class Hand {
    private List<Piece> wanPieces = Lists.newArrayList();
    private List<Piece> tongPieces = Lists.newArrayList();
    private List<Piece> tiaoPieces =Lists.newArrayList();
    private List<Piece> fengPieces = Lists.newArrayList();

    public List<Piece> getWanPieces() {
        return wanPieces;
    }

    public Hand setWanPieces(List<Piece> wanPieces) {
        this.wanPieces = wanPieces;
        return this;
    }

    public List<Piece> getTongPieces() {
        return tongPieces;
    }

    public Hand setTongPieces(List<Piece> tongPieces) {
        this.tongPieces = tongPieces;
        return this;
    }

    public List<Piece> getTiaoPieces() {
        return tiaoPieces;
    }

    public Hand setTiaoPieces(List<Piece> tiaoPieces) {
        this.tiaoPieces = tiaoPieces;
        return this;
    }

    public List<Piece> getFengPieces() {
        return fengPieces;
    }

    public Hand setFengPieces(List<Piece> fengPieces) {
        this.fengPieces = fengPieces;
        return this;
    }

    public boolean complete() {
        List<Group> pairs = pairs();
        for (Group group : pairs) {
            Hand hand = subtract(group);
            if (hand.well()) {
                return true;
            }
        }
        return false;
    }

    private boolean well() {
        return Pieces.well(wanPieces) && Pieces.well(tongPieces) && Pieces.well(tiaoPieces) && Pieces.well(fengPieces);
    }

    private boolean grouped(List<Piece> part) {
        if (part.size() == 2) {
            return part.get(0).equals(part.get(1));
        }

        return false;
    }

    private Hand subtract(Group group) {
        Hand hand = copy();
        switch (group.getKind()) {
            case WAN:
                Pieces.exclude(hand.wanPieces, group.getPieces());
                break;
            case TIAO:
                Pieces.exclude(hand.tiaoPieces, group.getPieces());
                break;
            case TONG:
                Pieces.exclude(hand.tongPieces, group.getPieces());
                break;
            case FENG:
                Pieces.exclude(hand.fengPieces, group.getPieces());
                break;
        }
        return hand;
    }

    private List<Group> pairs() {
        List<Group> pairs = Lists.newArrayList();
        pairs.addAll(Pieces.pairs(wanPieces));
        pairs.addAll(Pieces.pairs(fengPieces));
        pairs.addAll(Pieces.pairs(tiaoPieces));
        pairs.addAll(Pieces.pairs(tongPieces));
        return pairs;
    }

    public Hand copy() {
        Hand hand = new Hand();
        hand.wanPieces = Lists.newArrayList(wanPieces);
        hand.fengPieces = Lists.newArrayList(fengPieces);
        hand.tiaoPieces = Lists.newArrayList(tiaoPieces);
        hand.tongPieces = Lists.newArrayList(tongPieces);
        return hand;
    }

    public List<Piece> suit(Kind kind) {
        switch (kind) {
            case WAN:
                return wanPieces;
            case TIAO:
                return tiaoPieces;
            case TONG:
                return tongPieces;
            case FENG:
                return fengPieces;
        }
        return null;
    }

    public void discard(Piece piece) {
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, piece);
    }

    public void chi(Piece piece, Group group) {
        List<Piece> partners = group.partners(piece);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, partners);
    }

    public void peng(Piece piece) {
        List<Piece> partners = Pieces.repeat(piece, 2);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, partners);
    }

    public void feed(Piece piece) {
        List<Piece> pieces = suit(piece.getKind());
        Pieces.orderInsert(pieces, piece);
    }

    public void gang(Piece piece) {
        List<Piece> pieces = Pieces.repeat(piece, 3);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, pieces);
    }

    public void angang(Piece piece) {
        List<Piece> pieces = Pieces.repeat(piece, 4);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, pieces);
    }

    public boolean chiable(Piece piece) {
        return chiableSequences(piece).size() > 0;
    }

    public List<Group> chiableSequences(Piece piece) {
        List<Group> groups = Pieces.possibleSequences(piece);
        return $.filter(groups, group -> Pieces.contains(suit(piece.getKind()), group.partners(piece)));
    }

    public boolean pengable(Piece piece) {
        return Pieces.contains(suit(piece.getKind()), Pieces.repeat(piece, 2));
    }
}
