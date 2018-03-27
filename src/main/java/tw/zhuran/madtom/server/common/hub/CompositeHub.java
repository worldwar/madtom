package tw.zhuran.madtom.server.common.hub;

import com.github.underscore.$;
import com.github.underscore.Function1;
import tw.zhuran.madtom.server.common.Connection;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CompositeHub<T> extends Hubable {
    protected ConcurrentHashMap<T, Hubable> hubs = new ConcurrentHashMap<T, Hubable>();

    private Hub defaultHub = new Hub();

    protected Hubable hubable(T type) {
        Hubable hubable = hubs.get(type);
        if (hubable == null) {
            return defaultHub;
        }
        return hubable;
    }

    protected Hubable hubable(Connection connection) {
        return hubable(connection.getId());
    }

    protected Hubable hubable(long connection) {
        for (Hubable hubable : hubs.values()) {
            if (hubable.contains(connection)) {
                return hubable;
            }
        }
        return defaultHub;
    }

    @Override
    public void add(Connection connection) {
        defaultHub.add(connection);
    }

    @Override
    public void remove(long id) {
        Hubable hubable = hubable(id);
        hubable.remove(id);
    }

    @Override
    public boolean contains(long id) {
        return hubable(id).contains(id);
    }

    @Override
    public Connection get(long id) {
        return hubable(id).get(id);
    }

    @Override
    public Connection take() {
        for (Hubable hubable : hubs.values()) {
            if (hubable.size() > 0) {
                return hubable.take();
            }
        }
        if (defaultHub.size() > 0) {
            return defaultHub.take();
        }
        return null;
    }

    @Override
    public List<Connection> all() {
        List<Connection> collect = $.chain(hubs.values()).map(new Function1<Hubable, Object>() {
            @Override
            public Object apply(Hubable hubable) {
                return hubable.all();
            }
        }).flatten().value();
        collect.addAll(defaultHub.all());
        return collect;
    }

    @Override
    public int size() {
        int size = 0;
        for (Hubable hubable : hubs.values()) {
            size += hubable.size();
        }
        size += defaultHub.size();
        return size;
    }
}
