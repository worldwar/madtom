package tw.zhuran.madtom.domain;

import java.util.ArrayList;
import java.util.List;

public class Trunk {
    private Hand hand = new Hand();
    private List<Action> actions = new ArrayList<>();

    public Trunk(Hand hand) {
        this.hand = hand;
    }

    public Trunk(List<Piece> pieces) {
        init(pieces);
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void init(List<Piece> pieces) {
        hand.setWanPieces(Pieces.suit(pieces, Kind.WAN));
        hand.setTiaoPieces(Pieces.suit(pieces, Kind.TIAO));
        hand.setTongPieces(Pieces.suit(pieces, Kind.TONG));
        hand.setFengPieces(Pieces.suit(pieces, Kind.FENG));
    }

    public void feed(Piece piece) {
        hand.feed(piece);
    }

    public void discard(Piece piece) {
        hand.discard(piece);
        actions.add(Actions.discard(piece));
    }

    public void chi(Piece piece, Group group) {
        hand.chi(piece, group);
        actions.add(Actions.chi(piece, group));
    }

    public void peng(Piece piece, Group group) {
        hand.peng(piece, group);
        actions.add(Actions.peng(piece, group));
    }
}
