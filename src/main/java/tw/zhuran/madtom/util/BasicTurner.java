package tw.zhuran.madtom.util;

import java.util.List;

public class BasicTurner<T> extends AbstractTurner<T> {

    public BasicTurner(List<T> players) {
        super(players);
    }

    public BasicTurner(List<T> players, T initialTurn) {
        super(players, initialTurn);
    }

    @Override
    int nextIndex() {
        return (index + 1) % count();
    }

    @Override
    int previousIndex() {
        return (index - 1 + count()) % count();
    }
}
