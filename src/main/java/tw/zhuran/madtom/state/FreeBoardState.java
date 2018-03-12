package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;

public class FreeBoardState extends BoardState {
    public FreeBoardState(Board owner) {
        super(owner, BoardStateType.FREE);
    }

    @Override
    public BoardStateType perform(Event event) {
        Action action = event.getAction();
        if (action == null) {
            return this.type();
        }
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
