package tw.zhuran.madtom.domain;

public class Actions {
    public static Action discard(Piece piece) {
        return new Action(ActionType.DISCARD, piece, null);
    }

    public static Action chi(Piece piece, Group group) {
        return new Action(ActionType.CHI, piece, group);
    }

    public static Action peng(Piece piece) {
        return new Action(ActionType.PENG, piece, Pieces.triple(piece));
    }

    public static Action gang(Piece piece) {
        return new Action(ActionType.GANG, piece, Pieces.triple(piece));
    }

    public static Action angang(Piece piece) {
        return new Action(ActionType.ANGANG, piece, Pieces.triple(piece));
    }

    public static Action hongzhongGang() {
        return new Action(ActionType.HONGZHONG_GANG, Pieces.HONGZHONG, null);
    }

    public static Action laiziGang(Piece wildcard) {
        return new Action(ActionType.LAIZI_GANG, wildcard, null);
    }

    public static boolean genericPublicGang(ActionType type) {
        return type == ActionType.GANG ||
                type == ActionType.XUGANG ||
                type == ActionType.HONGZHONG_GANG ||
                type == ActionType.LAIZI_GANG;
    }

    public static boolean intercept(ActionType actionType) {
        return actionType == ActionType.CHI || actionType == ActionType.PENG || actionType == ActionType.GANG;
    }

    public static boolean free(ActionType actionType) {
        return actionType == ActionType.DISCARD || actionType == ActionType.XUGANG || actionType == ActionType.ANGANG || actionType == ActionType.HONGZHONG_GANG || actionType == ActionType.LAIZI_GANG;
    }
}
