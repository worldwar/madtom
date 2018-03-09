package tw.zhuran.madtom.util;

import java.util.List;

public class ReverseTurner extends AbstractTurner{
    public ReverseTurner(List players) {
        super(players);
    }

    public ReverseTurner(List players, Object initialTurn) {
        super(players, initialTurn);
    }

    @Override
    int nextIndex() {
        return (index - 1 + count()) % count();
    }

    @Override
    int previousIndex() {
        return (index + 1) % count();
    }
}
