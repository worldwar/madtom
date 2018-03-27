package tw.zhuran.madtom.server.common.hub;

import com.github.underscore.$;
import com.github.underscore.Block;
import com.github.underscore.Function1;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.server.common.Connection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReferenceHub extends ProxyHub {
    private Set<Long> connections = new HashSet<Long>();

    public ReferenceHub(Hubable hubable, Iterable<Connection> set) {
        super(hubable);
        $.each(set, new Block<Connection>() {
            @Override
            public void apply(Connection c) {
                ReferenceHub.this.connections.add(c.getId());
            }
        });
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
        return $.chain(connections).map(new Function1<Long, Connection>() {
            @Override
            public Connection apply(Long id) {
                return ReferenceHub.this.get(id);
            }
        }).filter(new Predicate<Connection>() {
            @Override
            public Boolean apply(Connection c) {
                return c != null;
            }
        }).value();
    }

    @Override
    public int size() {
        return connections.size();
    }
}
