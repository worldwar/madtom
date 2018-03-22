package tw.zhuran.madtom.server.common;

import io.netty.channel.ChannelFuture;
import tw.zhuran.madtom.server.common.hub.GameHub;
import tw.zhuran.madtom.server.common.hub.ReferenceHub;
import tw.zhuran.madtom.server.common.matcher.GameMatcher;

import java.util.List;
import java.util.concurrent.*;

public abstract class GameServer {
    private Listener listener;
    private ConcurrentMap<Long, GameContext> contexts;
    private ConcurrentMap<Long, GameContext> connectionContexts;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(8);
    private GameHub hub = new GameHub();
    private HandlerFactory handlerFactory;

    public GameServer() {
        contexts = new ConcurrentHashMap<>();
        connectionContexts = new ConcurrentHashMap<>();
    }

    public GameHub gameHub() {
        return hub;
    }

    public void start(int port) {
        try {
            listener = new Listener(handlerFactory);
            ChannelFuture future = listener.listen(port);
            initTasks();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
        }
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }
    public void initTasks() {
        submit(new GameMatcher(this, 4));
    }

    public void submit(Callable callable) {
        executor.submit(callable);
    }

    public void delay(Callable callable) {
        executor.schedule(callable, 2, TimeUnit.SECONDS);
    }

    public void newGame(List<Connection> connections) {
        while (true) {
            final GameContext context = newGameContext(connections);
            GameContext gameContext = contexts.putIfAbsent(context.getId(), context);
            if (gameContext == null) {
                connections.forEach(c -> connectionInContext(c.getId(), context));
                context.start();
                break;
            }
        }
    }

    protected GameContext newGameContext(List<Connection> connections) {
        long id = System.currentTimeMillis();
        ReferenceHub hub = new ReferenceHub(gameHub(), connections);
        return new GameContext(id, hub);
    }

    protected void connectionInContext(long id, final GameContext context) {
        connectionContexts.putIfAbsent(id, context);
    }

    protected void connectionOutContext(long id) {
        connectionContexts.remove(id);
    }

    protected GameContext findGameContextByConnection(long id) {
        return connectionContexts.get(id);
    }

    public void come(Connection connection) {
        hub.come(connection);
    }

    public void go(long id) {
        GameContext gameContext = connectionContexts.get(id);
        if (gameContext != null) {
            gameContext.remove(id);
        }
        hub.go(id);
    }

    public abstract void dispatch(long id, Packet packet);

    private void ready(Connection connection) {
        hub.enterMatching(connection);
    }

    private void unready(Connection connection) {
        hub.leaveMatching(connection);
    }

    public void close(GameContext gameContext) {
        contexts.remove(gameContext.getId());
    }

    private void exitGameContext(Connection c) {
        if (c != null) {
            connectionContexts.remove(c.getId());
        }
    }
}