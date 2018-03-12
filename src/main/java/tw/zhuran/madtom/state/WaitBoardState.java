package tw.zhuran.madtom.state;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.WaiterManager;
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
                owner.execute(activeEvent.getAction());
            }
        }
        return this.type();
    }
}
