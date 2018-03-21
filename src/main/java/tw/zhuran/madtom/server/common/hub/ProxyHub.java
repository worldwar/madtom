package tw.zhuran.madtom.server.common.hub;

import tw.zhuran.madtom.server.common.Connection;

import java.util.List;

public class ProxyHub implements Hubable {
    private Hubable hubable;

    public ProxyHub(Hubable hubable) {
        this.hubable = hubable;
    }

    @Override
    public void add(Connection connection) {
        hubable.add(connection);
    }

    @Override
    public void remove(long id) {
        hubable.remove(id);
    }

    @Override
    public boolean contains(long id) {
        return hubable.contains(id);
    }

    @Override
    public Connection get(long id) {
        return hubable.get(id);
    }

    @Override
    public Connection take() {
        return hubable.take();
    }

    @Override
    public List<Connection> all() {
        return hubable.all();
    }

    @Override
    public int size() {
        return hubable.size();
    }
}
