package tw.zhuran.madtom.event;

import tw.zhuran.madtom.domain.Piece;

public class GangAffordEvent extends Event {
    private Piece piece;

    public GangAffordEvent(int player, Piece piece) {
        super(EventType.GANG_AFFOARD, player);
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public GangAffordEvent duplicate() {
        return new GangAffordEvent(getPlayer(), piece);
    }
}
