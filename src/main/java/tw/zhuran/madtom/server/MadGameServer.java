package tw.zhuran.madtom.server;

import tw.zhuran.madtom.server.common.*;
import tw.zhuran.madtom.server.common.hub.ReferenceHub;
import tw.zhuran.madtom.server.packet.MadPacket;

import java.util.List;

public class MadGameServer extends GameServer {
    public MadGameServer() {
        super();
        setHandlerFactory(new MadHandlerFactory(this, new MadPacketFactory()));
    }


    protected GameContext newGameContext(List<Connection> connections) {
        long id = System.currentTimeMillis();
        ReferenceHub hub = new ReferenceHub(gameHub(), connections);
        return new MadGameContext(id, hub);
    }

    @Override
    public void dispatch(long id, Packet packet) {
        MadGameContext context = (MadGameContext)findGameContextByConnection(id);

        MadPacket madPacket = (MadPacket) packet;

        if (context != null) {
            switch (madPacket.getType()) {
                case EVENT:
                    EventPacket eventPacket = (EventPacket) packet;
                    context.perform(eventPacket.getContent());
            }
        } else {
            Connection connection = gameHub().get(id);
            switch (madPacket.getType()) {
                case READY:
                    boolean ready = ready(connection);
                    if (ready) {
                        N.send(connection, Packets.ready());
                    }
                    break;
                case UNREADY:
                    boolean unready = unready(connection);
                    if (unready) {
                        N.send(connection, Packets.unready());
                    }
                    break;
            }
        }
    }
}
