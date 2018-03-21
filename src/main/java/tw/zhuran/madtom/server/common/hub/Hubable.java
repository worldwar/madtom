package tw.zhuran.madtom.server.common.hub;

import tw.zhuran.madtom.server.common.Connection;

import java.util.List;

public interface Hubable {
    void add(Connection connection);

    default void addAll(List<Connection> connections) {
        connections.forEach(this::add);
    }

    default void remove(Connection connection) {
        if (connection != null) {
            remove(connection.getId());
        }
    }

    void remove(long id);

    default boolean contains(Connection connection) {
        if (connection != null) {
            return contains(connection.getId());
        }
        return false;
    }

    boolean contains(long id);

    Connection get(long id);

    Connection take();

    List<Connection> all();

    int size();
}
