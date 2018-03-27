package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.github.underscore.Block;
import com.github.underscore.Function1;
import com.github.underscore.Predicate;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.rule.*;
import tw.zhuran.madtom.util.F;

import java.util.ArrayList;
import java.util.List;

public class Trunk {
    static List<PlotRule> plotRules;
    private int player;
    private Hand hand = new Hand();
    private List<Action> actions = new ArrayList<>();
    private TriggerType triggerType = TriggerType.CAPTURE;

    static {
        plotRules = Lists.newArrayList();
        plotRules.add(HardRule.instance);
        plotRules.add(PengRule.instance);
        plotRules.add(SuitRule.instance);
        plotRules.add(FengRule.instance);
        plotRules.add(JiangRule.instance);
        plotRules.add(BegRule.instance);
    }

    public Trunk(int player) {
        this.player = player;
    }

    public Trunk(Hand hand) {
        this.hand = hand;
    }

    public Trunk(List<Piece> pieces) {
        init(pieces);
    }

    public int player() {
        return player;
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

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
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

    public boolean discardable(Piece piece) {
        return hand.discardable(piece);
    }

    public void discard(Piece piece) {
        hand.discard(piece);
        actions.add(Actions.discard(piece));
    }

    public boolean chiable(Piece piece) {
        return hand.chiable(piece);
    }

    public boolean chiable(Piece piece, Group group) {
        return hand.chiable(piece, group);
    }

    public void chi(Piece piece, Group group) {
        hand.chi(piece, group);
        actions.add(Actions.chi(piece, group));
    }

    public boolean pengable(Piece piece) {
        return hand.pengable(piece);
    }

    public void peng(Piece piece) {
        hand.peng(piece);
        actions.add(Actions.peng(piece));
    }

    public boolean gangable(Piece piece) {
        return hand.gangable(piece);
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

    public Action findPeng(final Piece piece) {
        List<Action> actions = $.filter(filterActions(ActionType.PENG), new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return action.getPiece().equals(piece);
            }
        });
        if (actions.size() == 0) {
            return null;
        } else {
            return actions.get(0);
        }
    }

    public List<Action> filterActions(final ActionType type) {
        return $.filter(this.actions, new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return action.getType() == type;
            }
        });
    }

    public boolean angangable(Piece piece) {
        return hand.angangable(piece);
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
        final List<Piece> pieces = hand.pieces();
        return $.chain(filterActions(ActionType.PENG)).map(new Function1<Action, Piece>() {
            @Override
            public Piece apply(Action action) {
                return action.getPiece();
            }
        }).filter(new Predicate<Piece>() {
            @Override
            public Boolean apply(Piece o) {
                return pieces.contains(o);
            }
        }).value();
    }

    public Plot plot(Form form) {
        final Plot plot = new Plot();
        plot.setForm(form);
        plot.setActions(actions);
        plot.trigger(triggerType);
        $.each(rules(), new Block<PlotRule>() {
            @Override
            public void apply(PlotRule rule) {
                rule.apply(plot);
            }
        });
        return plot;
    }

    public List<Plot> plots() {
        return $.map(hand.shiftForms(), new Function1<Form, Plot>() {
            @Override
            public Plot apply(Form form) {
                return Trunk.this.plot(form);
            }
        });
    }

    private List<PlotRule> rules() {
        return plotRules;
    }

    public void deal(List<Piece> pieces) {
        $.each(pieces, new Block<Piece>() {
            @Override
            public void apply(Piece piece) {
                hand.feed(piece);
            }
        });
    }

    public boolean opened() {
        return $.any(actions, new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return Pieces.opened(action);
            }
        });
    }

    public List<Plot> activePlots(Piece piece, TriggerType triggerType) {
        List<Plot> plots = Lists.newArrayList();
        if (!opened()) {
            return plots;
        }

        if (hand.getHongzhongPieces().size() != 0) {
            return plots;
        }
        Hand copy = hand.copy();
        if (piece != null) {
            hand.feed(piece);
            this.triggerType = triggerType;
        }
        plots = plots();
        if (plots.size() != 0) {
            plots = $.filter(plots, new Predicate<Plot>() {
                @Override
                public Boolean apply(Plot plot) {
                    return plot.featured() || plot.countOfWildcards() <= 1;
                }
            });
            plots = $.filter(plots, new Predicate<Plot>() {
                @Override
                public Boolean apply(Plot plot) {
                    return plot.isFeng() || plot.isJiang() || plot.isPeng() || plot.isSuit() || Pieces.isJiang(plot.pair().getPieces().get(0));
                }
            });
        } else {
            if (feng()) {
                Plot plot = new Plot();
                plot.setFeng(true);
                plot.setHard(hand.getWildcard().getKind() == Kind.FENG);
                plot.setHand(hand);
                plot.trigger(triggerType);
                plots.add(plot);
            } else if (jiang()){
                Plot plot = new Plot();
                plot.setJiang(true);
                plot.setHard(Pieces.isJiang(hand.getWildcard()));
                plot.setHand(hand);
                plot.trigger(triggerType);
                plots.add(plot);
            }
        }
        if (piece != null) {
            hand = copy;
        }
        return plots;
    }

    public Plot bestPlot(Piece piece, TriggerType triggerType) {
        List<Plot> plots = activePlots(piece, triggerType);
        if (plots.size() == 0) {
            return null;
        }
        return $.max(plots, new Function1<Plot, Comparable>() {
            @Override
            public Comparable apply(Plot plot) {
                return plot.base();
            }
        });
    }

    public List<Group> actionGroups() {
        return $.chain(actions).filter(new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return Pieces.hasGroup(action);
            }
        }).map(new Function1<Action, Group>() {
            @Override
            public Group apply(Action action) {
                return action.getGroup();
            }
        }).value();
    }

    public boolean feng() {
        return $.all(hand.pieces(), new Predicate<Piece>() {
            @Override
            public Boolean apply(Piece p) {
                return p.getKind() == Kind.FENG;
            }
        }) && $.all(actionGroups(), new Predicate<Group>() {
            @Override
            public Boolean apply(Group group) {
                return group.getKind() == Kind.FENG;
            }
        });
    }

    public boolean jiang() {
        return $.all(hand.pieces(), new Predicate<Piece>() {
            @Override
            public Boolean apply(Piece piece) {
                return Pieces.isJiang(piece);
            }
        }) && $.all(groupActions(), new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return Pieces.isJiang(action.getPiece());
            }
        });
    }

    public List<Action> groupActions() {
        return $.filter(actions, new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return Pieces.hasGroup(action);
            }
        });
    }

    public int score() {
        int exponent = 0;
        if (opened()) {
            exponent++;
        }
        exponent += $.filter(actions, new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return Actions.genericPublicGang(action.getType());
            }
        }).size();
        exponent += $.filter(actions, new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return action.getType() == ActionType.ANGANG;
            }
        }).size() * 2;
        return F.power(2, exponent);
    }

    public boolean hongzhongGangable() {
        return hand.hongzhongGangable();
    }

    public boolean laiziGangable() {
        return hand.laiziGangable();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("hand: " + Pieces.string(hand.pieces()) + "\n");
        builder.append("wildcard: " + Pieces.string(hand.getWildcards()) + "\n");
        builder.append("hongzhong: " + Pieces.string(hand.getHongzhongPieces()) + "\n");
        builder.append("=========\n");
        List<Action> a = $.filter(this.actions, new Predicate<Action>() {
            @Override
            public Boolean apply(Action action) {
                return action.getType() != ActionType.DISCARD;
            }
        });
        builder.append(Actions.string(a));
        return builder.toString();
    }
}
