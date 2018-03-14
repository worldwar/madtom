package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.util.F;

import java.util.List;

public class Hand {
    private Piece wildcard;
    private List<Piece> wildcards = Lists.newArrayList();
    private List<Piece> hongzhongPieces = Lists.newArrayList();
    private List<Piece> wanPieces = Lists.newArrayList();
    private List<Piece> tongPieces = Lists.newArrayList();
    private List<Piece> tiaoPieces =Lists.newArrayList();
    private List<Piece> fengPieces = Lists.newArrayList();

    public Piece getWildcard() {
        return wildcard;
    }

    public List<Piece> getWildcards() {
        return wildcards;
    }

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

    public List<Piece> getHongzhongPieces() {
        return hongzhongPieces;
    }

    public Hand setHongzhongPieces(List<Piece> hongzhongPieces) {
        this.hongzhongPieces = hongzhongPieces;
        return this;
    }

    public boolean complete() {
        return forms().size() > 0;
    }

    public boolean finish() {
        List<List<Piece>> combinations = Pieces.combinations(wildcards.size(), partners());
        if (combinations.size() == 0) {
            return complete();
        }
        return $.any(combinations, pieces -> {
            Hand hand = copy();
            hand.shift(pieces);
            return hand.complete();
        });
    }

    private void shift(List<Piece> pieces) {
        $.each(pieces, this::add);
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
        return Pieces.pairs(pieces());
    }

    public Hand copy() {
        Hand hand = new Hand();
        hand.wildcard = wildcard;
        hand.wildcards = Lists.newArrayList(wildcards);
        hand.wanPieces = Lists.newArrayList(wanPieces);
        hand.fengPieces = Lists.newArrayList(fengPieces);
        hand.tiaoPieces = Lists.newArrayList(tiaoPieces);
        hand.tongPieces = Lists.newArrayList(tongPieces);
        hand.hongzhongPieces = Lists.newArrayList(hongzhongPieces);
        return hand;
    }

    public List<Piece> pieces() {
        List<Piece> pieces = Lists.newArrayList();
        pieces.addAll(wanPieces);
        pieces.addAll(tiaoPieces);
        pieces.addAll(tongPieces);
        pieces.addAll(fengPieces);
        return pieces;
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

    public boolean discardable(Piece piece) {
        return !(piece.equals(wildcard) || piece.equals(Pieces.HONGZHONG)) && $.any(pieces(), piece::equals);
    }

    public void discard(Piece piece) {
        if (piece.equals(wildcard)) {
            Pieces.exclude(wildcards, piece);
        } else if (piece.equals(Pieces.HONGZHONG)) {
            Pieces.exclude(hongzhongPieces, piece);
        } else {
            List<Piece> suit = suit(piece.getKind());
            Pieces.exclude(suit, piece);
        }
    }

    public void chi(Piece piece, Group group) {
        List<Piece> partners = group.partners(piece);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, partners);
    }

    public void peng(Piece piece) {
        List<Piece> partners = F.repeat(piece, 2);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, partners);
    }

    public void feed(Piece piece) {
        if (piece.equals(wildcard)) {
            wildcards.add(wildcard);
        } else if (piece.equals(Pieces.HONGZHONG)) {
            hongzhongPieces.add(piece);
        } else {
            add(piece);
        }
    }

    private void add(Piece piece) {
        List<Piece> pieces = suit(piece.getKind());
        Pieces.orderInsert(pieces, piece);
    }

    public void gang(Piece piece) {
        List<Piece> pieces = F.repeat(piece, 3);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, pieces);
    }

    public void angang(Piece piece) {
        List<Piece> pieces = F.repeat(piece, 4);
        List<Piece> suit = suit(piece.getKind());
        Pieces.exclude(suit, pieces);
    }

    public void hongzhongGang() {
        assert hongzhongPieces.size() > 0;
        Pieces.exclude(hongzhongPieces, Pieces.HONGZHONG);
    }

    public void laiziGang() {
        assert wildcards.size() > 0;
        Pieces.exclude(wildcards, wildcard);
    }

    public boolean chiable(Piece piece) {
        return chiableSequences(piece).size() > 0;
    }

    public boolean chiable(Piece piece, Group group) {
        return chiableSequences(piece).contains(group);
    }

    public List<Group> chiableSequences(Piece piece) {
        List<Group> groups = Pieces.possibleSequences(piece);
        return $.filter(groups, group -> Pieces.contains(suit(piece.getKind()), group.partners(piece)));
    }

    public boolean pengable(Piece piece) {
        return Pieces.contains(suit(piece.getKind()), F.repeat(piece, 2));
    }

    public boolean gangable(Piece piece) {
        return Pieces.contains(suit(piece.getKind()), F.repeat(piece, 3));
    }

    public boolean angangable() {
        return angangablePieces().size() > 0;
    }

    public boolean angangable(Piece piece) {
        return angangablePieces().contains(piece);
    }

    public List<Piece> angangablePieces() {
        return Pieces.countLargeThan(pieces(), 3);
    }

    public void setWildcard(Piece wildcard) {
        this.wildcard = wildcard;
        wildcards = $.filter(pieces(), piece -> piece.equals(wildcard));
        Pieces.exclude(suit(wildcard.getKind()), wildcards);
    }

    public List<Piece> partners() {
        return $.chain(pieces()).map(piece -> Pieces.partners(piece)).flatten().uniq().value();
    }

    public List<Form> forms() {
        List<Form> forms = Lists.newArrayList();
        List<Group> pairs = pairs();
        for (Group group : pairs) {
            Form form = new Form();
            form.add(group);
            Hand hand = subtract(group);
            forms.addAll(hand.forms(form));
        }
        return forms;
    }

    public List<Form> shiftForms() {
        List<List<Piece>> combinations = Pieces.combinations(wildcards.size(), partners());
        if (combinations.size() == 0) {
            return forms();
        }

        return $.chain(combinations).map(combination -> {
            Hand hand = copy();
            hand.shift(combination);
            Shift shift = new Shift(wildcard, combination);
            List<Form> forms = hand.forms();
            $.each(forms, form -> form.setShift(shift));
            return forms;
        }).flatten().value();
    }

    private List<Form> forms(Form form) {
        if (pieces().size() == 0) {
            return Lists.newArrayList(form);
        } else {
            List<Form> forms = Lists.newArrayList();
            List<Group> sentences = headSentences();
            for (Group group : sentences) {
                if (form.addable(group)) {
                    Form copy = form.copy();
                    copy.add(group);
                    Hand hand = subtract(group);
                    forms.addAll(hand.forms(copy));
                }
            }
            return forms;
        }
    }

    public List<Group> headSentences() {
        for (Kind kind : Kind.values()) {
            List<Piece> suit = suit(kind);
            if (suit.size() != 0) {
                return Pieces.headSentences(suit);
            }
        }
        return Lists.newArrayList();
    }

    public boolean hongzhongGangable() {
        return hongzhongPieces.size() > 0;
    }

    public boolean laiziGangable() {
        return wildcards.size() > 0;
    }
}
