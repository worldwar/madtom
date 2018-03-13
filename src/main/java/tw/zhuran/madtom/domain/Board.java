package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;
import tw.zhuran.madtom.rule.Rules;
import tw.zhuran.madtom.rule.WaitRule;
import tw.zhuran.madtom.state.BoardStateManager;
import tw.zhuran.madtom.state.BoardStateType;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.NaturalTurner;
import tw.zhuran.madtom.util.R;
import tw.zhuran.madtom.util.Results;

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
    private Result result;

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
        if (bottom()) {
            trunk().setTriggerType(TriggerType.BOTTOM);
        }
    }

    public void gangAfford() {
        int dice = R.dice();
        deck.gangAfford(dice);
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
    }

    public boolean shouldWait(Action action) {
        List<WaitRule> waitRules = Rules.discardWaitRules();
        return $.any(waitRules, rule -> rule.shouldWait(this, action));
    }

    public void turnNext() {
        turner.turnNext();
    }

    public int turn() {
        return turner.current();
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
        return otherTrunks(turn());
    }

    public List<Trunk> otherTrunks(int player) {
        return $.chain(trunks.entrySet()).filter(entry -> entry.getKey() != player).map(entry -> entry.getValue()).value();
    }

    public boolean winnable(Trunk winner, Trunk loser, Action action) {
        int score = score(winner, loser, action);
        return score >= 16;
    }

    public boolean winnable(int player) {
        return true;
    }

    public boolean currentWinnable() {
        return winnable(turner.current());
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

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void intercept(Event event) {
        Action action = event.getAction();
        if (event.getEventType() == EventType.WIN) {
            result = makeResult(event);
        } else if (action != null) {
            if (Actions.intercept(action.getType())) {
                turner.turnTo(event.getPlayer());
                execute(action);
            } else if (action.getType() == ActionType.XUGANG) {
                gangAfford();
            }
        }
    }

    private Result makeResult(Event event) {
        int winner = event.getPlayer();
        List<Trunk> trunks = otherTrunks(winner);
        Trunk winnerTrunk = trunks.get(winner);
        Score score = new Score();
        Action action = event.getAction();
        Plot plot = winnerTrunk.bestPlot(action.getPiece(), Actions.triggerType(action.getType()));
        assert plot != null;

        int winnerPoint = 0;
        for (Trunk trunk : trunks) {
            int point = score(winnerTrunk, trunk, action);
            score.put(trunk.player(), -point);
            winnerPoint += point;
        }
        score.put(winner, winnerPoint);
        return Results.win(winner, plot, score);
    }

    public boolean bottom() {
        int remainPillars = deck.remainPillars();
        return remainPillars <= 10 && remainPillars > 8;
    }

    public void settle() {

    }

    public void draw() {
        result = Results.draw();
    }
}
