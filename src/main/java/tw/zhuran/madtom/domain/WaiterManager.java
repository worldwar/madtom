package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;
import tw.zhuran.madtom.rule.ChiWaitRule;
import tw.zhuran.madtom.rule.PengWaitRule;
import tw.zhuran.madtom.rule.WinWaitRule;

import java.util.List;

public class WaiterManager {
    private Board board;
    private List<Confirmation> winners = Lists.newArrayList();
    private List<Confirmation> pengWaiters = Lists.newArrayList();
    private List<Confirmation> gangWaiters = Lists.newArrayList();
    private List<Confirmation> chiWaiters = Lists.newArrayList();
    private boolean timeout;
    private Action waitAction;
    private Action chiAction;

    public WaiterManager(Board board) {
        this.board = board;
        reset();
    }

    public void findWaiters(Action action) {
        reset();
        winners = initConfirmation(WinWaitRule.instance.waiters(board, action));
        pengWaiters = initConfirmation(PengWaitRule.instance.waiters(board, action));
        gangWaiters = initConfirmation(PengWaitRule.instance.waiters(board, action));
        chiWaiters = initConfirmation(ChiWaitRule.instance.waiters(board, action));
        this.waitAction = action;
    }

    private List<Confirmation> initConfirmation(List<Integer> waiters) {
        return $.map(waiters, waiter -> new Confirmation(waiter, ConfirmState.UNCONFIRM));
    }

    private void reset() {
        winners = Lists.newArrayList();
        pengWaiters = Lists.newArrayList();
        gangWaiters = Lists.newArrayList();
        chiWaiters = Lists.newArrayList();
        timeout = false;
        waitAction = null;
        chiAction = null;
    }

    public void accept(Event event) {
        EventType eventType = event.getEventType();
        int player = event.getPlayer();
        if (eventType == EventType.PASS) {
            pass(winners, player);
            pass(pengWaiters, player);
            pass(gangWaiters, player);
            pass(chiWaiters, player);
        } else if (eventType == EventType.WIN) {
            confirm(winners, player);
            pass(pengWaiters, player);
            pass(gangWaiters, player);
            pass(chiWaiters, player);
        } else if (eventType == EventType.ACTION) {
            Action action = event.getAction();
            ActionType actionType = action.getType();
            if (actionType == ActionType.PENG) {
                confirm(pengWaiters, player);
                pass(winners, player);
                pass(gangWaiters, player);
                pass(chiWaiters, player);
            } else if (actionType == ActionType.GANG) {
                confirm(gangWaiters, player);
                pass(winners, player);
                pass(pengWaiters, player);
                pass(chiWaiters, player);
            } else if (actionType == ActionType.CHI) {
                confirm(chiWaiters, player);
                pass(winners, player);
                pass(pengWaiters, player);
                pass(gangWaiters, player);
                chiAction = action;
            }
        } else if (eventType == EventType.WAIT_TIMEOUT) {
            timeout = true;
        }
    }

    public boolean shouldWait() {
        boolean shouldWaitWin;
        if (winners.size() > 0) {
            Confirmation confirmation = winners.get(0);
            if (confirmation.state == ConfirmState.CONFIRMED) {
                return false;
            }
        }
        shouldWaitWin = $.any(winners, winner -> winner.state == ConfirmState.UNCONFIRM);
        if (shouldWaitWin) {
            return true;
        }
        boolean shouldWaitPeng = $.any(pengWaiters, waiter -> waiter.state == ConfirmState.UNCONFIRM);
        boolean shouldWaitGang = $.any(gangWaiters, waiter -> waiter.state == ConfirmState.UNCONFIRM);
        return shouldWaitPeng || shouldWaitGang || $.any(chiWaiters, wait -> wait.state == ConfirmState.UNCONFIRM);
    }

    public Event activeEvent() {
        Event event = activeEvent(winners, EventType.WIN, null);
        if (event != null) {
            return event;
        }

        event = activeEvent(pengWaiters, EventType.ACTION, Actions.peng(waitAction.getPiece()));

        if (event != null) {
            return event;
        }

        event = activeEvent(gangWaiters, EventType.ACTION, Actions.gang(waitAction.getPiece()));

        if (event != null) {
            return event;
        }

        event = activeEvent(chiWaiters, EventType.ACTION, chiAction);

        if (event != null) {
            return event;
        }
        return null;
    }

    public Event activeEvent(List<Confirmation> waiters, EventType eventType, Action action) {
        Optional<Confirmation> option = $.find(waiters, waiter -> waiter.state == ConfirmState.CONFIRMED);
        if (option.isPresent()) {
//                return new Event(eventType, , option.get().player);
            return new Event(eventType, action, option.get().player);
        }
        return null;
    }

    private void pass(List<Confirmation> winners, int player) {
        setState(winners, player, ConfirmState.PASS);
    }

    private void confirm(List<Confirmation> winners, int player) {
        setState(winners, player, ConfirmState.CONFIRMED);
    }

    private void setState(List<Confirmation> waiters, int player, ConfirmState state) {
        Confirmation c = findPlayer(waiters, player);
        if (c != null) {
            c.state = state;
        }
    }

    private Confirmation findPlayer(List<Confirmation> waiters, int player) {
        Optional<Confirmation> o = $.find(waiters, waiter -> waiter.player == player);
        return o.orNull();
    }

    class Confirmation {
        public Confirmation(int player, ConfirmState state) {
            this.player = player;
            this.state = state;
        }

        public int player;
        public ConfirmState state;
    }

    enum ConfirmState {
        UNCONFIRM,
        CONFIRMED,
        PASS
    }
}
