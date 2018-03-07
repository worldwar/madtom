package tw.zhuran.madtom.domain;

import com.github.underscore.$;

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

    public void setWildcard(Piece piece) {
        hand.setWildcard(piece);
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

    public void peng(Piece piece) {
        hand.peng(piece);
        actions.add(Actions.peng(piece));
    }

    public void gang(Piece piece) {
        hand.gang(piece);
        actions.add(Actions.gang(piece));
    }

    public void xugang(Piece piece) {
        hand.discard(piece);
        Action action = findPeng(piece);
        if (action != null) {
            action.xugang();
        }
    }

    public Action findPeng(Piece piece) {
        List<Action> actions = $.filter(filterActions(ActionType.PENG), action -> action.getPiece().equals(piece));
        if (actions.size() == 0) {
            return null;
        } else {
            return actions.get(0);
        }
    }

    public List<Action> filterActions(ActionType type) {
        return $.filter(this.actions, action -> action.getType() == type);
    }

    public void angang(Piece piece) {
        hand.angang(piece);
        actions.add(Actions.angang(piece));
    }

    public void hongzhongGang() {
        hand.hongzhongGang();
        actions.add(Actions.hongzhongGang());
    }

    public void laiziGang() {
        hand.laiziGang();
        actions.add(Actions.laiziGang(hand.getWildcard()));
    }

    public boolean xugangable() {
        return xugangablePieces().size() > 0;
    }

    public boolean xugangable(Piece piece) {
        return xugangablePieces().contains(piece);
    }

    public List<Piece> xugangablePieces() {
        List<Piece> pieces = hand.pieces();
        return $.chain(filterActions(ActionType.PENG)).map(Action::getPiece).filter(pieces::contains).value();
    }
}
