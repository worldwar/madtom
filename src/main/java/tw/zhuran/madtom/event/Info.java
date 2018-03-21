package tw.zhuran.madtom.event;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Piece;

import java.util.List;
import java.util.Map;

public class Info {
    private List<Piece> pieces;
    private List<Action> actions;
    private Map<Integer, Integer> otherHandCounts;
    private Map<Integer, List<Action>> otherActions;
    private int turn;
    private int self;
    private int dealer;
    private Piece wildcard;
    private int remainPillars;

    public List<Piece> getPieces() {
        return pieces;
    }

    public Info setPieces(List<Piece> pieces) {
        this.pieces = pieces;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public Info setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    public Map<Integer, Integer> getOtherHandCounts() {
        return otherHandCounts;
    }

    public Info setOtherHandCounts(Map<Integer, Integer> otherHandCounts) {
        this.otherHandCounts = otherHandCounts;
        return this;
    }

    public Map<Integer, List<Action>> getOtherActions() {
        return otherActions;
    }

    public Info setOtherActions(Map<Integer, List<Action>> otherActions) {
        this.otherActions = otherActions;
        return this;
    }

    public int getTurn() {
        return turn;
    }

    public Info setTurn(int turn) {
        this.turn = turn;
        return this;
    }

    public int getSelf() {
        return self;
    }

    public Info setSelf(int self) {
        this.self = self;
        return this;
    }

    public int getDealer() {
        return dealer;
    }

    public Info setDealer(int dealer) {
        this.dealer = dealer;
        return this;
    }

    public Piece getWildcard() {
        return wildcard;
    }

    public Info setWildcard(Piece wildcard) {
        this.wildcard = wildcard;
        return this;
    }

    public int getRemainPillars() {
        return remainPillars;
    }

    public Info setRemainPillars(int remainPillars) {
        this.remainPillars = remainPillars;
        return this;
    }
}
