package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.github.underscore.Block;
import com.github.underscore.Function1;
import com.github.underscore.Predicate;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;
import tw.zhuran.madtom.event.Events;
import tw.zhuran.madtom.event.Info;
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
import java.util.Observer;

public class Board {
    private Deck deck;
    private Map<Integer, Trunk> trunks = new HashMap<>();
    private int players;
    private Piece wildcard;
    private NaturalTurner turner;
    private BoardStateManager stateManager = new BoardStateManager(this);
    private Result result;
    private int dealer;
    private Observer observer;

    public Board(int players) {
        this.players = players;
        deck = new Deck(players);
        $.each(F.list($.range(1, 5)), new Block<Integer>() {
            @Override
            public void apply(Integer index) {
                Trunk trunk = new Trunk(index);
                trunks.put(index, trunk);
            }
        });
        this.turner = new NaturalTurner(players);
    }

    public void shuffle() {
        result = null;
        deck = new Deck(players);
        $.each(F.list($.range(1, 5)), new Block<Integer>() {
            @Override
            public void apply(Integer index) {
                Trunk trunk = new Trunk(index);
                trunks.put(index, trunk);
            }
        });

        int dealer = R.dealer();
        setDealer(dealer);
        int one = R.dice();
        int two = R.dice();

        int start = (one + two) % players + 1;
        int index = Math.max(one, two);
        cut(start, index);
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
        turner.turnTo(dealer);
    }

    public void cut(int start, int index) {
        deck.cut(start, index);
        Pieces.times(new Runnable() {
            @Override
            public void run() {
                Board.this.dealNext();
            }
        }, 3 * players);
        Pieces.times(new Runnable() {
            @Override
            public void run() {
                Board.this.lastDealNext();
            }
        }, players);
        dispatch0();
        declareWildcard();
        stateManager.init(BoardStateType.FREE);
    }

    public Trunk trunk() {
        return trunks.get(turner.current());
    }

    public Trunk trunk(int player) {
        return trunks.get(player);
    }

    public void dealNext() {
        List<Piece> pieces = deck.deal();
        trunk().deal(pieces);
        turner.turnNext();
    }

    private Piece dispatch0() {
        Piece piece = deck.afford();
        trunk().feed(piece);
        trunk().setTriggerType(TriggerType.SELF);
        if (bottom()) {
            trunk().setTriggerType(TriggerType.BOTTOM);
        }
        return piece;
    }

    public void dispatch() {
        Piece piece = dispatch0();
        Event dispatch = Events.dispatch(turn(), piece);
        notifyEvent(dispatch);
    }

    public void gangAfford() {
        int dice = R.dice();
        Piece piece = deck.gangAfford(dice);
        trunk().feed(piece);
        trunk().setTriggerType(TriggerType.FIRE);
        notifyEvent(Events.gangAfford(turn(), piece));
    }

    public void notifyEvent(Object event) {
        if (observer != null) {
            observer.update(null, event);
        }
    }

    public void lastDealNext() {
        dispatch();
        turner.turnNext();
    }

    public void declareWildcard() {
        Piece piece = deck.afford();
        wildcard = Pieces.wildcard(piece);
        $.each(trunks.values(), new Block<Trunk>() {
            @Override
            public void apply(Trunk trunk) {
                trunk.setWildcard(wildcard);
            }
        });
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

    public boolean shouldWait(final Action action) {
        List<WaitRule> waitRules = Lists.newArrayList();
        if (action.getType() == ActionType.DISCARD) {
            waitRules = Rules.discardWaitRules();
        } else if (action.getType() == ActionType.XUGANG){
            waitRules = Rules.xugangWaitRules();
        }
        return $.any(waitRules, new Predicate<WaitRule>() {
            @Override
            public Boolean apply(WaitRule rule) {
                return rule.shouldWait(Board.this, action);
            }
        });
    }

    public void turnNext() {
        turner.turnNext();
    }

    public int turn() {
        return turner.current();
    }

    public int dealer() {
        return dealer;
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
        notifyEvent(Events.action(turn(), action));
    }

    public BoardStateType state() {
        return stateManager.currentState();
    }

    public List<Trunk> otherTrunks() {
        return otherTrunks(turn());
    }

    public List<Trunk> otherTrunks(final int player) {
        return $.chain(trunks.entrySet()).filter(new Predicate<Map.Entry<Integer, Trunk>>() {
            @Override
            public Boolean apply(Map.Entry<Integer, Trunk> entry) {
                return entry.getKey() != player;
            }
        }).map(new Function1<Map.Entry<Integer, Trunk>, Trunk>() {
            @Override
            public Trunk apply(Map.Entry<Integer, Trunk> entry) {
                return entry.getValue();
            }
        }).value();
    }

    public List<Trunk> otherOrderedTrunks() {
        List<Integer> players = turner.ordered();
        players.remove(0);
        return $.map(players, new Function1<Integer, Trunk>() {
            @Override
            public Trunk apply(Integer player) {
                return trunks.get(player);
            }
        });
    }

    public boolean winnable(Trunk winner, Trunk loser, Action action) {
        int score = score(winner, loser, action, true);
        return score >= 16;
    }

    public boolean winnable(int player) {
        final Trunk winner = trunk(player);
        List<Trunk> otherTrunks = otherTrunks(player);
        return $.any(otherTrunks, new Predicate<Trunk>() {
            @Override
            public Boolean apply(Trunk loser) {
                return Board.this.score(winner, loser) >= 16;
            }
        });
    }

    public boolean currentWinnable() {
        return winnable(turner.current());
    }

    private int score(Trunk winner, Trunk loser) {
        Plot plot = trunk().bestPlot(null, null);
        if (plot == null) {
            return 0;
        }
        return score(plot, winner, loser, false);
    }

    private int score(Trunk trunk, Trunk player, Action action, boolean isTrigger) {
        Plot plot = null;
        if (action.getType() == ActionType.DISCARD) {
            plot = trunk.bestPlot(action.getPiece(), TriggerType.CAPTURE);
        } else if (action.getType() == ActionType.DISCARD) {
            plot = trunk.bestPlot(action.getPiece(), TriggerType.RUSH);
        }
        if (plot != null) {
            if (isTrigger) {
                return score(plot, trunk, player, true);
            } else {
                return score(plot, trunk, player, false);
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
            settle(event);
        } else if (action != null) {
            if (Actions.intercept(action.getType())) {
                turner.turnTo(event.getPlayer());
                execute(action);
                if (action.getType() == ActionType.GANG) {
                    gangAfford();
                }
            } else if (action.getType() == ActionType.XUGANG) {
                gangAfford();
            }
        }
    }

    private Result makeResult(Event event) {
        int winner = event.getPlayer();
        List<Trunk> otherTrunks = otherTrunks(winner);
        Trunk winnerTrunk = trunks.get(winner);
        Score score = new Score();
        Action action = event.getAction();
        Plot plot = winnerTrunk.bestPlot(action.getPiece(), Actions.triggerType(action.getType()));
        assert plot != null;

        int winnerPoint = 0;
        for (Trunk trunk : otherTrunks) {
            int point = score(winnerTrunk, trunk, action, trunk.player() == turn());
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
        int winner = turn();
        List<Trunk> otherTrunks = otherTrunks(winner);
        Trunk winnerTrunk = trunks.get(winner);
        Score score = new Score();
        Plot plot = winnerTrunk.bestPlot(null, null);
        assert plot != null;

        int winnerPoint = 0;
        for (Trunk trunk : otherTrunks) {
            int point = score(winnerTrunk, trunk);
            score.put(trunk.player(), -point);
            winnerPoint += point;
        }
        score.put(winner, winnerPoint);
        result = Results.win(winner, plot, score);
    }

    public void settle(Event event) {
        result = makeResult(event);
    }

    public void draw() {
        result = Results.draw();
    }

    public Result getResult() {
        return result;
    }

    public Piece waitPiece() {
        if (stateManager.currentState() == BoardStateType.WAIT) {
            return stateManager.waitPiece();
        }
        return null;
    }

    public Info info(final int player) {
        Info info = new Info();
        Trunk playerTrunk = trunk(player);
        info.setActions(playerTrunk.getActions());
        info.setPieces(playerTrunk.getHand().all());

        final Map<Integer, List<Action>> otherActions = new HashMap<>();
        final Map<Integer, Integer> otherHandCounts = new HashMap<>();
        $.each(trunks.values(), new Block<Trunk>() {
            @Override
            public void apply(Trunk trunk) {
                if (trunk.player() != player) {
                    otherActions.put(trunk.player(), trunk.getActions());
                }
            }
        });
        $.each(trunks.values(), new Block<Trunk>() {
            @Override
            public void apply(Trunk trunk) {
                if (trunk.player() != player) {
                    otherHandCounts.put(trunk.player(), trunk.getHand().size());
                }
            }
        });
        info.setOtherActions(otherActions);
        info.setOtherHandCounts(otherHandCounts);

        info.setDealer(dealer);
        info.setRemainPillars(deck.remainPillars());
        info.setSelf(player);
        info.setTurn(turn());
        info.setWildcard(wildcard);
        return info;
    }

    public void register(Observer observer) {
        this.observer = observer;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("癞子: " + wildcard.toString() + "\n");
        builder.append("剩余墩数: " + deck.remainPillars() + "\n");
        builder.append(stateManager.toString());
        return builder.toString();
    }
}
