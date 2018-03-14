package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;

public class WaitBoardState extends BoardState {
    private WaiterManager waiterManager;

    public WaitBoardState(Board owner) {
        super(owner, BoardStateType.WAIT);
         waiterManager = new WaiterManager(owner);
    }

    @Override
    public BoardStateType perform(Event event) {
        waiterManager.accept(event);
        if (waiterManager.shouldWait()) {
            return this.type();
        } else {
            Event activeEvent = waiterManager.activeEvent();
            if (activeEvent != null) {
                owner.intercept(activeEvent);
                switch (activeEvent.getEventType()) {
                    case ACTION:
                        if (activeEvent.getAction().getType() == ActionType.XUGANG) {
                            return BoardStateType.FREE;
                        } else {
                            return BoardStateType.OPEN;
                        }
                    case WIN:
                        return BoardStateType.CLOSE;
                }
            } else {
                return BoardStateType.DISPATCH;
            }
        }
        return this.type();
    }

    @Override
    public void begin(Event event) {
        Action action = event.getAction();
        if (action != null) {
            waiterManager.findWaiters(event);
        }
    }

    public Piece waitPiece() {
        return waiterManager.waitPiece();
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("state: Wait\n");
        builder.append(waiterManager.toString() + "\n");
        return builder.toString();

    }
}
