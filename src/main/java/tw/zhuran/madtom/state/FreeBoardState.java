package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;

public class FreeBoardState extends BoardState {
    public FreeBoardState(Board owner) {
        super(owner, BoardStateType.FREE);
    }

    @Override
    public BoardStateType perform(Action action) {
        ActionType type = action.getType();
        Trunk trunk = owner.trunk();
        Piece piece = action.getPiece();
        if (
                (type == ActionType.DISCARD && trunk.discardable(piece)) ||
                (type == ActionType.XUGANG && trunk.xugangable(piece)) ||
                (type == ActionType.ANGANG && trunk.angangable(piece)) ||
                (type == ActionType.HONGZHONG_GANG && trunk.hongzhongGangable()) ||
                (type == ActionType.LAIZI_GANG && trunk.laiziGangable())
            ) {
            owner.execute(action);
            return BoardStateType.WAIT;
        }
        return this.type;
    }
}
