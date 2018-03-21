package tw.zhuran.madtom.server.common;

import tw.zhuran.madtom.server.common.hub.Hubable;

import java.util.List;

public class GameContext {
    private final long id;
    private Hubable hub;

    public GameContext(long id, Hubable hubable) {
        this.id = id;
        this.hub = hubable;
    }

    public long getId() {
        return id;
    }

    public void notify(Packet packet) {
        notify(packet, hub.all());
    }

    private void notify(Packet packet, List<Connection> connections) {
        connections.stream()
                .filter(connection -> connection != null)
                .forEach(connection -> notify(packet, connection));
    }

    private void notify(Packet packet, Connection connection) {
        connection.send(packet.bytes());
    }

    public void add(Connection connection) {
        hub.add(connection);
    }

    public void add(Connection... connections) {
        for (Connection c : connections) {
            hub.add(c);
        }
    }

    public void start() {

    }

    public void remove(Connection connection) {
        hub.remove(connection);
    }

    public void remove(long id) {
        hub.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GameContext that = (GameContext) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}