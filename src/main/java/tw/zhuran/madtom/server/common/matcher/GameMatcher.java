package tw.zhuran.madtom.server.common.matcher;

import tw.zhuran.madtom.server.common.Connection;
import tw.zhuran.madtom.server.common.GameServer;
import tw.zhuran.madtom.server.common.hub.GameHub;

import java.util.List;
import java.util.concurrent.Callable;

public class GameMatcher implements Callable<Void> {
    private GameServer gameServer;
    private int size;

    public GameMatcher(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public GameMatcher(GameServer gameServer, int size) {
        this.gameServer = gameServer;
        this.size = size;
    }

    @Override
    public Void call() throws Exception {
        GameHub gameHub = gameServer.gameHub();
        if (gameHub.matchingSize() >= size) {
            List<Connection> connections = gameHub.enterInGame(size);
            gameServer.newGame(connections);
        }
        gameServer.delay(this);
        return null;
    }
}
