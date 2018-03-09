package tw.zhuran.madtom.util;

import java.util.List;

public abstract class AbstractTurner<T> implements Turner<T> {
    protected List<T> players;
    protected int index;

    public AbstractTurner(List<T> players) {
        this.players = players;
        turnTo(players.get(0));
    }

    public AbstractTurner(List<T> players, T initialTurn) {
        this.players = players;
        turnTo(initialTurn);
    }

    @Override
    public void turnTo(T t) {
        index = indexOf(t);
    }

    protected int indexOf(T player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(player)) {
                return i;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public T current() {
        return get(index);
    }

    @Override
    public T next() {
        return get(nextIndex());
    }

    @Override
    public T previous() {
        return get(previousIndex());
    }

    @Override
    public void turnNext() {
        index = nextIndex();
    }

    protected T get(int index) {
        return players.get(index);
    }

    protected int count() {
        return players.size();
    }

    abstract int nextIndex();

    abstract int previousIndex();
}
