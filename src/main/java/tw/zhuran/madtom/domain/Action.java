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

    public void xugang() {
        if (type == ActionType.PENG) {
            type = ActionType.XUGANG;
        }
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        switch (type) {
            case DISCARD:
                build.append("打");
                break;
            case PENG:
                build.append("碰");
                break;
            case GANG:
                build.append("杠");
                break;
            case XUGANG:
                build.append("蓄杠");
                break;
            case ANGANG:
                build.append("暗杠");
                break;
            case HONGZHONG_GANG:
                build.append("红中杠");
                break;
            case LAIZI_GANG:
                build.append("赖子杠");
                break;
        }

        build.append(" " + piece);
        if (group != null) {
            build.append(" " + Pieces.string(group.getPieces()));
        }
        return build.toString();
    }
}
