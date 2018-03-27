package tw.zhuran.madtom.util;

import com.google.common.collect.Lists;

import java.util.List;

public class ReverseTurner<T> extends AbstractTurner<T> {
    public ReverseTurner(List players) {
        super(players);
    }

    @Override
    int nextIndex() {
        return (index - 1 + count()) % count();
    }

    @Override
    int previousIndex() {
        return (index + 1) % count();
    }

    @Override
    public List<T> ordered() {
        return Lists.newArrayList();
    }
}
