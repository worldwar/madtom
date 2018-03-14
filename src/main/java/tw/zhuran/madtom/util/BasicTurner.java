package tw.zhuran.madtom.util;

import com.google.common.collect.Lists;

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
    public List<T> ordered() {
        List<T> list = Lists.newArrayList();
        T current = current();
        list.add(current);
        int i = index;
        while ((i = nextIndex(i)) != index) {
            list.add(get(i));
        }
        return list;
    }

    protected int nextIndex(int i) {
        return (i + 1) % count();
    }

    @Override
    int previousIndex() {
        return (index - 1 + count()) % count();
    }
}
