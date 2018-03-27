package tw.zhuran.madtom.server.common.hub;

import com.github.underscore.$;
import com.github.underscore.Block;
import tw.zhuran.madtom.server.common.Connection;

import java.util.List;

public abstract class Hubable {
    public abstract void add(Connection connection);

    public void addAll(List<Connection> connections) {
        $.each(connections, new Block<Connection>() {
            @Override
            public void apply(Connection connection) {
                Hubable.this.add(connection);
            }
        });
    }

    public void remove(Connection connection) {
        if (connection != null) {
            remove(connection.getId());
        }
    }

    public abstract void remove(long id);

    public boolean contains(Connection connection) {
        if (connection != null) {
            return contains(connection.getId());
        }
        return false;
    }

    public abstract boolean contains(long id);

    public abstract Connection get(long id);

    public abstract Connection take();

    public abstract List<Connection> all();

    public abstract int size();
}
