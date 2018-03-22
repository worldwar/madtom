package tw.zhuran.madtom.server;

import tw.zhuran.madtom.server.common.GameServer;
import tw.zhuran.madtom.server.common.Packet;
import tw.zhuran.madtom.server.packet.MadPacket;

public class MadGameServer extends GameServer {
    public MadGameServer() {
        super();
        setHandlerFactory(new MadHandlerFactory(this, new MadPacketFactory()));
    }

    @Override
    public void dispatch(long id, Packet packet) {
        MadGameContext context = (MadGameContext)findGameContextByConnection(id);

        MadPacket madPacket = (MadPacket) packet;

        if (context != null) {
            switch (madPacket.type()) {
                case EVENT:
                    EventPacket eventPacket = (EventPacket) packet;
                    context.perform(eventPacket.getContent());
            }
        }
    }
}
