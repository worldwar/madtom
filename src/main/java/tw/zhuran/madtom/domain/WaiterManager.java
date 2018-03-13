package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;
import tw.zhuran.madtom.rule.ChiWaitRule;
import tw.zhuran.madtom.rule.GangWaitRule;
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
    private Event waitEvent;
    private Action chiAction;

    public WaiterManager(Board board) {
        this.board = board;
        reset();
    }

    public void findWaiters(Event event) {
        Action action = event.getAction();
        reset();
        winners = initConfirmation(WinWaitRule.instance.waiters(board, action));
        pengWaiters = initConfirmation(PengWaitRule.instance.waiters(board, action));
        gangWaiters = initConfirmation(GangWaitRule.instance.waiters(board, action));
        chiWaiters = initConfirmation(ChiWaitRule.instance.waiters(board, action));
        this.waitAction = action;
        this.waitEvent = event;
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
        if (winners.size() > 0) {
            int firstWinIndex = -1;
            int index = 0;
            for (Confirmation confirmation : winners) {
                if (confirmation.state == ConfirmState.CONFIRMED) {
                    firstWinIndex = index;
                    break;
                }
                index++;
            }
            if (firstWinIndex == -1) {
                firstWinIndex = index;
            }
            List<Confirmation> slice = winners.subList(0, firstWinIndex);
            boolean anyUnconfirmed = $.any(slice, confirmation -> confirmation.state == ConfirmState.UNCONFIRM);

            if (firstWinIndex != winners.size()) {
                return anyUnconfirmed;
            } else {
                if (anyUnconfirmed) {
                    return true;
                }
            }
        }

        boolean shouldFinishWaitGang = $.any(gangWaiters, waiter -> waiter.state == ConfirmState.CONFIRMED);
        boolean shouldWaitGang = $.any(gangWaiters, waiter -> waiter.state == ConfirmState.UNCONFIRM);

        if (shouldFinishWaitGang) {
            return false;
        }

        if (shouldWaitGang) {
            return true;
        }

        boolean shouldFinishWaitPeng = $.any(pengWaiters, waiter -> waiter.state == ConfirmState.CONFIRMED);
        boolean shouldWaitPeng = $.any(pengWaiters, waiter -> waiter.state == ConfirmState.UNCONFIRM);

        if (shouldFinishWaitPeng) {
            return false;
        }

        if (shouldWaitPeng) {
            return true;
        }

        boolean shouldWaitchi = $.any(chiWaiters, waiter -> waiter.state == ConfirmState.UNCONFIRM);

        if (shouldWaitchi) {
            return true;
        }
        return false;
    }

    public Event activeEvent() {
        Event event = activeEvent(winners, EventType.WIN, waitAction);
        if (event != null) {
            return event;
        }

        if (waitAction.getType() == ActionType.XUGANG) {
            return waitEvent;
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

    public WaiterManager setWinners(List<Integer> winners) {
        this.winners = initConfirmation(winners);
        return this;
    }

    public WaiterManager setPengWaiters(List<Integer> pengWaiters) {
        this.pengWaiters = initConfirmation(pengWaiters);
        return this;
    }

    public WaiterManager setGangWaiters(List<Integer> gangWaiters) {
        this.gangWaiters = initConfirmation(gangWaiters);
        return this;
    }

    public WaiterManager setChiWaiters(List<Integer> chiWaiters) {
        this.chiWaiters = initConfirmation(chiWaiters);
        return this;
    }

    public void setWaitAction(Action action) {
        this.waitAction = action;
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
