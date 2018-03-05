package tw.zhuran.madtom.domain;

public class Actions {
    public static Action discard(Piece piece) {
        return new Action(ActionType.DISCARD, piece, null);
    }

    public static Action chi(Piece piece, Group group) {
        return new Action(ActionType.CHI, piece, group);
    }

    public static Action peng(Piece piece, Group group) {
        return new Action(ActionType.PENG, piece, group);
    }
}
