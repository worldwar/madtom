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
}
