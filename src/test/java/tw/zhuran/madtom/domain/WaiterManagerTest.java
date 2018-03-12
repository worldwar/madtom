package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import tw.zhuran.madtom.event.EventType;
import tw.zhuran.madtom.event.Events;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class WaiterManagerTest {

    private WaiterManager waiterManager;

    @Before
    public void before() {
        waiterManager = new WaiterManager(new Board(4));

        waiterManager.setWaitAction(Actions.discard(Pieces.YIWAN));
    }

    @Test
    public void shouldFinishWhenFirstWinnerConfirm() {
        waiterManager.setWinners(Lists.newArrayList(2, 3, 4));
        waiterManager.accept(Events.win(2));
        assertFalse(waiterManager.shouldWait());
        assertThat(waiterManager.activeEvent().getEventType(), is(EventType.WIN));
    }

    @Test
    public void shouldWaitWhenFirstWinnerUnconfirm() {
        waiterManager.setWinners(Lists.newArrayList(2, 3, 4));
        waiterManager.accept(Events.win(3));
        assertTrue(waiterManager.shouldWait());
    }

    @Test
    public void shouldFinishWhenWinnerBeforeTheConfirmedWinnerAllPassed() {
        waiterManager.setWinners(Lists.newArrayList(2, 3, 4));
        waiterManager.accept(Events.win(3));
        waiterManager.accept(Events.pass(2));
        assertFalse(waiterManager.shouldWait());
    }

    @Test
    public void shouldWaitWhenThereAreWinnerUnconfirmBeforeFirstConfirm() {
        waiterManager.setWinners(Lists.newArrayList(2, 3, 4));
        waiterManager.accept(Events.pass(2));
        waiterManager.accept(Events.win(4));
        assertTrue(waiterManager.shouldWait());
    }

    @Test
    public void shouldWaitWhenThereAreWinnerUnconfirm() {
        waiterManager.setWinners(Lists.newArrayList(2, 3, 4));
        waiterManager.accept(Events.pass(3));
        waiterManager.accept(Events.pass(2));
        assertTrue(waiterManager.shouldWait());
    }

    @Test
    public void shouldFinishWhenAllWinnersPass() {
        waiterManager.setWinners(Lists.newArrayList(2, 3, 4));
        waiterManager.accept(Events.pass(3));
        waiterManager.accept(Events.pass(2));
        waiterManager.accept(Events.pass(4));
        assertFalse(waiterManager.shouldWait());
        assertNull(waiterManager.activeEvent());
    }

    @Test
    public void shouldFinishWhenThereAreNoWinner() {
        waiterManager.setWinners(Lists.newArrayList());
        assertFalse(waiterManager.shouldWait());
        assertNull(waiterManager.activeEvent());
    }

    @Test
    public void shouldWaitWhenThereArePengWaiterUnconfirm() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        assertTrue(waiterManager.shouldWait());
    }

    @Test
    public void shouldFinishWhenThereArePengWaiterConfirmed() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        Action peng = Actions.peng(Pieces.YIWAN);
        waiterManager.accept(Events.action(2, peng));
        assertFalse(waiterManager.shouldWait());
        assertThat(waiterManager.activeEvent().getAction().getType(), is(ActionType.PENG));
    }

    @Test
    public void shouldFinishWhenThereArePengWaiterPassed() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        waiterManager.accept(Events.pass(2));
        assertFalse(waiterManager.shouldWait());
        assertNull(waiterManager.activeEvent());
    }

    @Test
    public void shouldWaitWhenThereArePengWaiterUnconfirmedAndChiConfirmed() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        waiterManager.setChiWaiters(Lists.newArrayList(3));
        Action chi = Actions.chi(Pieces.YIWAN, Pieces.sequence(Pieces.YIWAN, 1));
        waiterManager.accept(Events.action(3, chi));
        assertTrue(waiterManager.shouldWait());
    }

    @Test
    public void shouldFinishWhenPengWaiterAndChiWaiterAreConfirmedOrPass() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        waiterManager.setChiWaiters(Lists.newArrayList(3));
        Action chi = Actions.chi(Pieces.YIWAN, Pieces.sequence(Pieces.YIWAN, 1));
        waiterManager.accept(Events.action(3, chi));
        waiterManager.accept(Events.pass(2));
        assertFalse(waiterManager.shouldWait());
        assertThat(waiterManager.activeEvent().getAction().getType(), is(ActionType.CHI));
    }

    @Test
    public void shouldFinishWhenPengWaiterAndChiWaiterAreSameAndChiWaiterConfirmedOrPass() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        waiterManager.setChiWaiters(Lists.newArrayList(2));
        Action chi = Actions.chi(Pieces.YIWAN, Pieces.sequence(Pieces.YIWAN, 1));
        waiterManager.accept(Events.action(2, chi));
        assertFalse(waiterManager.shouldWait());
        assertThat(waiterManager.activeEvent().getAction().getType(), is(ActionType.CHI));
    }

    @Test
    public void shouldFinishWhenPengWaiterAndChiWaiterAreSameAndPengWaiterConfirmedOrPass() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        waiterManager.setChiWaiters(Lists.newArrayList(2));
        waiterManager.accept(Events.pass(2));
        assertFalse(waiterManager.shouldWait());
        assertNull(waiterManager.activeEvent());
    }

    @Test
    public void shouldFinishWhenPengWaiterConfirmAndUnConfirmed() {
        waiterManager.setPengWaiters(Lists.newArrayList(2));
        waiterManager.setChiWaiters(Lists.newArrayList(3));
        Action peng = Actions.peng(Pieces.YIWAN);
        waiterManager.accept(Events.action(2, peng));
        assertFalse(waiterManager.shouldWait());
    }
}
