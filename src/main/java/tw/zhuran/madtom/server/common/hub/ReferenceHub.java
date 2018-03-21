package tw.zhuran.madtom.server.common.hub;

import tw.zhuran.madtom.server.common.Connection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReferenceHub extends ProxyHub {
    private Set<Long> connections = new HashSet<Long>();

    public ReferenceHub(Hubable hubable, Iterable<Connection> set) {
        super(hubable);
        set.forEach(c -> this.connections.add(c.getId()));
    }

    @Override
    public void add(Connection connection) {
        connections.add(connection.getId());
    }

    @Override
    public void remove(long id) {
        connections.remove(id);
    }

    @Override
    public boolean contains(long id) {
        return connections.contains(id) && super.contains(id);
    }

    @Override
    public Connection get(long id) {
        if (connections.contains(id)) {
            return super.get(id);
        }
        return null;
    }

    @Override
    public List<Connection> all() {
        return connections.stream().map(this::get).filter(c -> c != null).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return connections.size();
    }
}
