package tw.zhuran.madtom.domain;

public class Piece {
    private Kind kind;
    private int index;

    public Piece(Kind kind, int index) {
        this.kind = kind;
        this.index = index;
    }

    public Kind getKind() {
        return kind;
    }

    public Piece setKind(Kind kind) {
        this.kind = kind;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Piece setIndex(int index) {
        this.index = index;
        return this;
    }

    public Piece prev() {
        return new Piece(kind, index - 1);
    }

    public Piece next() {
        return new Piece(kind, index + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Piece piece = (Piece) o;

        if (index != piece.index)
            return false;
        return kind == piece.kind;

    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        if (kind != Kind.FENG) {
            String unit = "";
            switch (kind) {
                case WAN:
                    unit = "万";
                    break;
                case TIAO:
                    unit = "条";
                    break;
                case TONG:
                    unit = "筒";
                    break;
            }
            return index + unit;
        }
        return "";
    }
}
