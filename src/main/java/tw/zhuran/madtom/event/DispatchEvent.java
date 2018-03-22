package tw.zhuran.madtom.event;

import tw.zhuran.madtom.domain.Piece;

public class DispatchEvent extends Event {
    private Piece piece;

    public DispatchEvent(int player, Piece piece) {
        super(EventType.DISPATCH, player);
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public DispatchEvent duplicate() {
        return new DispatchEvent(getPlayer(), piece);
    }
}
