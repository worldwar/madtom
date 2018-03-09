package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.NaturalTurner;

import java.util.List;
import java.util.Map;

public class Board {
    private Deck deck;
    private Map<Integer, Trunk> trunks;
    private int players;
    private Piece wildcard;
    private NaturalTurner turner;

    public Board(int players) {
        this.players = players;
        deck = new Deck(players);
        trunks = F.index(F.multiple(Trunk::new, 4));
        this.turner = new NaturalTurner(players);
    }

    public void setDealer(int dealer) {
        turner.turnTo(dealer);
    }

    public void cut(int start, int index) {
        deck.cut(start, index);
        Pieces.times(this::dealNext, 3 * players);
        Pieces.times(this::lastDealNext, players);
        dispatch();
    }

    public Trunk trunk() {
        return trunks.get(turner.current());
    }

    public void dealNext() {
        List<Piece> pieces = deck.deal();
        trunk().deal(pieces);
        turner.turnNext();
    }

    public void dispatch() {
        trunk().feed(deck.afford());
    }

    public void lastDealNext() {
        dispatch();
        turner.turnNext();
    }

    public void declareWildcard() {
        Piece piece = deck.afford();
        wildcard = Pieces.wildcard(piece);
        $.each(trunks.values(), (trunk) -> trunk.setWildcard(wildcard));
    }
}
