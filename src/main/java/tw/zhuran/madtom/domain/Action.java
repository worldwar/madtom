package tw.zhuran.madtom.domain;

public class Action {
    private ActionType type;
    private Piece piece;
    private Group group;

    public Action(ActionType type, Piece piece, Group group) {
        this.type = type;
        this.piece = piece;
        this.group = group;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
