package tw.zhuran.madtom.server;

import tw.zhuran.madtom.server.common.GameServer;
import tw.zhuran.madtom.server.common.Packet;

public class MadGameServer extends GameServer {
    public MadGameServer() {
        super();
        setHandlerFactory(new MadHandlerFactory(this, new MadPacketFactory()));
    }

    @Override
    public void dispatch(long id, Packet packet) {

    }
}
