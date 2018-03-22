package tw.zhuran.madtom.server.common.hub;

import tw.zhuran.madtom.server.common.Connection;
import tw.zhuran.madtom.server.common.MatcherState;

import java.util.ArrayList;
import java.util.List;

public class GameHub extends CompositeHub<MatcherState> {
    public GameHub() {
        hubs.put(MatcherState.FREE, new Hub());
        hubs.put(MatcherState.MATCHING, new Hub());
        hubs.put(MatcherState.IN_GAME, new Hub());
    }

    public Hubable free() {
        return hubs.get(MatcherState.FREE);
    }

    public boolean isFree(Connection connection) {
        return hubable(connection) == free();
    }

    public Hubable matching() {
        return hubs.get(MatcherState.MATCHING);
    }

    public boolean isMatching(Connection connection) {
        return hubable(connection) == matching();
    }

    public Hubable inGame() {
        return hubs.get(MatcherState.IN_GAME);
    }

    public boolean isInGame(Connection connection) {
        return hubable(connection) == inGame();
    }

    public boolean enterMatching(Connection connection) {
        synchronized (this) {
            if (isFree(connection)) {
                free().remove(connection);
                matching().add(connection);
            }
            return isMatching(connection);
        }
    }

    public boolean leaveMatching(Connection connection) {
        synchronized (this) {
            if (isMatching(connection)) {
                matching().remove(connection);
                free().add(connection);
            }
            return !isMatching(connection);
        }
    }

    public void leaveGame(Connection connection) {
        synchronized (this) {
            if (isInGame(connection)) {
                inGame().remove(connection);
                free().add(connection);
            }
        }
    }

    public List<Connection> enterInGame(int size) {
        synchronized (this) {
            Hubable matching = matching();
            Hubable inGame = inGame();
            if (matching.size() >= size) {
                List<Connection> list = new ArrayList<>();
                while (list.size() < size) {
                    Connection c = matching.take();
                    inGame.add(c);
                    list.add(c);
                }
                return list;
            }
        }
        return new ArrayList<>();
    }

    public void come(Connection connection) {
        synchronized (this) {
            free().add(connection);
        }
    }

    public void go(Connection connection) {
        synchronized (this) {
            remove(connection);
        }
    }

    public void go(long id) {
        synchronized (this) {
            remove(id);
        }
    }

    public int matchingSize() {
        return matching().size();
    }
}
