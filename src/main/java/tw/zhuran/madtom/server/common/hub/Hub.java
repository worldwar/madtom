package tw.zhuran.madtom.server.common.hub;

import com.google.common.collect.Lists;
import tw.zhuran.madtom.server.common.Connection;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Hub implements Hubable {
    private ConcurrentMap<Long, Connection> connections = new ConcurrentHashMap<>();

    public void add(Connection connection) {
        connections.putIfAbsent(connection.getId(), connection);
    }

    public void addAll(List<Connection> connections) {
        connections.forEach(this::add);
    }

    public void remove(Connection connection) {
        if (connection != null) {
            remove(connection.getId());
        }
    }

    public void remove(long id) {
        connections.remove(id);
    }

    public boolean contains(Connection connection) {
        if (connection != null) {
            return contains(connection.getId());
        }
        return false;
    }

    public boolean contains(long id) {
        return connections.containsKey(id);
    }

    public Connection get(long id) {
        return connections.get(id);
    }

    @Override
    public Connection take() {
        synchronized (this) {
            Iterator<Connection> iterator = connections.values().iterator();
            if (iterator.hasNext()) {
                Connection next = iterator.next();
                iterator.remove();
                return next;
            }
        }
        return null;
    }

    public List<Connection> all() {
        return Lists.newArrayList(connections.values());
    }

    @Override
    public int size() {
        return connections.size();
    }
}