package tw.zhuran.madtom.domain;

import java.util.List;

public class Group {
    private List<Piece> pieces;
    private GroupType groupType;
    private Kind kind;

    public Group(List<Piece> pieces, GroupType groupType, Kind kind) {
        this.pieces = pieces;
        this.groupType = groupType;
        this.kind = kind;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Group setPieces(List<Piece> pieces) {
        this.pieces = pieces;
        return this;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public Group setGroupType(GroupType groupType) {
        this.groupType = groupType;
        return this;
    }

    public Kind getKind() {
        return kind;
    }

    public Group setKind(Kind kind) {
        this.kind = kind;
        return this;
    }

    public List<Piece> partners(Piece piece) {
        return Pieces.subtract(pieces, piece);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (pieces != null ? !pieces.equals(group.pieces) : group.pieces != null) return false;
        if (groupType != group.groupType) return false;
        return kind == group.kind;
    }

    @Override
    public int hashCode() {
        int result = pieces != null ? pieces.hashCode() : 0;
        result = 31 * result + (groupType != null ? groupType.hashCode() : 0);
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        return result;
    }
}
