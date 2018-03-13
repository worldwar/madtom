package tw.zhuran.madtom.state;

import org.junit.Before;
import org.junit.Test;
import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.EventType;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class StateTest {

    private Board board;

    private Deck deck;
    @Before
    public void before() {
        board = new Board(4);
        deck = new Deck(Pieces.deck(), 4);
        board.setDeck(deck);
    }

    @Test
    public void shouldSwitchToFreeAfterDeal() {
        board.cut(1, 1);
        assertThat(board.state(), is(BoardStateType.FREE));
    }

    @Test
    public void shouldSwitchToWaitAfterDiscardIfThereArePengWaiters() {
        board.setDealer(2);
        board.cut(1, 1);
        Action discard = Actions.discard(Pieces.ERWAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.WAIT));
    }

    @Test
    public void shouldSwitchToFreeAfterThePengWaiterPass() {
        board.setDealer(2);
        board.cut(1, 1);
        Action discard = Actions.discard(Pieces.ERWAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        event = new Event(EventType.PASS, null, 3);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.FREE));
    }

    @Test
    public void shouldSwitchToOpenAfterThePengWaiterConfirm() {
        board.setDealer(2);
        board.cut(1, 1);
        Action discard = Actions.discard(Pieces.ERWAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        Action peng = Actions.peng(Pieces.ERWAN);
        event = new Event(EventType.ACTION, peng, 3);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.OPEN));
    }

    @Test
    public void shouldSwitchToOpenAfterTheChiWaiterConfirm() {
        board.setDealer(2);
        board.cut(1, 1);
        Action discard = Actions.discard(Pieces.YIWAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        Action chi = Actions.chi(Pieces.YIWAN, Pieces.sequence(Pieces.YIWAN, 1));
        event = new Event(EventType.ACTION, chi, 3);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.OPEN));
    }

    @Test
    public void shouldStayWaitAfterTheChiWaiterConfirmWhenThereArePengWaiterUnconfirm() {
        board.setDealer(2);
        board.cut(1, 1);
        Action discard = Actions.discard(Pieces.WUWAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        Action chi = Actions.chi(Pieces.WUWAN, Pieces.sequence(Pieces.WUWAN, 1));
        event = new Event(EventType.ACTION, chi, 3);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.WAIT));
    }

    @Test
    public void shouldSwitchToWaitAfterDiscardIfThereAreNoChiWaiters() {
        board.setDealer(2);
        board.cut(1, 1);
        Action discard = Actions.discard(Pieces.YIWAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.WAIT));
    }

    @Test
    public void shouldSwitchToFreeAfterDiscardIfThereAreNoWaiters() {
        board.setDealer(2);
        board.cut(4, 16);
        Action discard = Actions.discard(Pieces.BAIBAN);
        Event event = new Event(EventType.ACTION, discard, 2);
        board.perform(event);
        assertThat(board.state(), is(BoardStateType.FREE));
    }
}