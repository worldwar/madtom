package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.rule.Rules;
import tw.zhuran.madtom.rule.WaitRule;
import tw.zhuran.madtom.state.BoardStateManager;
import tw.zhuran.madtom.state.BoardStateType;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.NaturalTurner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Deck deck;
    private Map<Integer, Trunk> trunks = new HashMap<>();
    private int players;
    private Piece wildcard;
    private NaturalTurner turner;
    private BoardStateManager stateManager = new BoardStateManager(this);

    public Board(int players) {
        this.players = players;
        deck = new Deck(players);
        $.each(F.list($.range(1, 5)), index -> {
            Trunk trunk = new Trunk(index);
            trunks.put(index, trunk);
        });
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
        stateManager.init(BoardStateType.FREE);
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

    public int score(Plot plot, Trunk winner, Trunk loser, boolean capture) {
        int score = plot.base() * winner.score() * loser.score();
        if (capture) {
            if (plot.featured()) {
                score = score * 3 / 2;
            } else {
                score = score * 2;
            }
        }
        return score;
    }

    public void perform(Event event) {
        stateManager.perform(event);
        if (stateManager.currentState() == BoardStateType.WAIT) {
            Action action = event.getAction();
            if (action != null) {
                boolean shouldWait = shouldWait(action);
                if (!shouldWait) {
                    dispatch();
                    turnNext();
                }
            }
        }
    }

    private boolean shouldWait(Action action) {
        List<WaitRule> waitRules = Rules.discardWaitRules();
        return $.any(waitRules, rule -> rule.shouldWait(this, action));
    }

    private void turnNext() {
        turner.turnNext();
    }

    public void execute(Action action) {
        Trunk trunk = trunk();
        Piece piece = action.getPiece();
        Group group = action.getGroup();
        switch (action.getType()) {
            case DISCARD:
                trunk.discard(piece);
                break;
            case CHI:
                trunk.chi(piece, group);
                break;
            case PENG:
                trunk.peng(piece);
                break;
            case GANG:
                trunk.gang(piece);
                break;
            case XUGANG:
                trunk.xugang(piece);
                break;
            case ANGANG:
                trunk.angang(piece);
                break;
            case HONGZHONG_GANG:
                trunk.hongzhongGang();
                break;
            case LAIZI_GANG:
                trunk.laiziGang();
        }
    }

    public BoardStateType state() {
        return stateManager.currentState();
    }

    public List<Trunk> otherTrunks() {
        int player = turner.current();
        return $.chain(trunks.entrySet()).filter(entry -> entry.getKey() != player).map(entry -> entry.getValue()).value();
    }

    public boolean winnable(Trunk winner, Trunk loser, Action action) {
        int score = score(winner, loser, action);
        return score >= 16;
    }

    private int score(Trunk trunk, Trunk player, Action action) {
        if (action.getType() == ActionType.DISCARD) {
            Plot plot = trunk.bestPlot(action.getPiece(), TriggerType.CAPTURE);
            if (plot != null) {
                return score(plot, trunk, player, true);
            } else {
                return 0;
            }
        }
        return 0;
    }

    public Trunk nextTrunk() {
        return trunks.get(turner.next());
    }
}
