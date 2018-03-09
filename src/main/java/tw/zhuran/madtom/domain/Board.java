package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import tw.zhuran.madtom.util.F;

import java.util.List;
import java.util.Map;

public class Board {
    private Deck deck;
    private Map<Integer, Trunk> trunks;
    private int dealer;
    private int turn;
    private int players;
    private Piece wildcard;

    public Board(int players) {
        this.players = players;
        deck = new Deck(players);
        trunks = F.index(F.multiple(Trunk::new, 4));
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
        this.turn = dealer;
    }

    public void cut(int start, int index) {
        deck.cut(start, index);
        Pieces.times(this::dealNext, 3 * players);
        Pieces.times(this::lastDealNext, players);
        dispatch();
    }

    public Trunk trunk() {
        return trunks.get(turn);
    }

    public void dealNext() {
        List<Piece> pieces = deck.deal();
        trunk().deal(pieces);
        turnNext();
    }

    public void dispatch() {
        trunk().feed(deck.afford());
    }

    public void lastDealNext() {
        dispatch();
        turnNext();
    }

    private void turnNext() {
        turnTo(next(turn));
    }

    private void turnTo(int player) {
        turn = player;
    }

    public void declareWildcard() {
        Piece piece = deck.afford();
        wildcard = Pieces.wildcard(piece);
        $.each(trunks.values(), (trunk) -> trunk.setWildcard(wildcard));
    }

    public int next(int turn) {
        if (turn == players) {
            return 1;
        } else {
            return turn + 1;
        }
    }

    public int previous(int turn) {
        if (turn == 1) {
            return players;
        } else {
            return turn - 1;
        }
    }
}
