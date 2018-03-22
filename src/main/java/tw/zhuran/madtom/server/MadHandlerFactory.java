package tw.zhuran.madtom.server;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import tw.zhuran.madtom.server.common.GameServer;
import tw.zhuran.madtom.server.common.HandlerFactory;
import tw.zhuran.madtom.server.common.PacketFactory;
import tw.zhuran.madtom.server.common.PacketHandler;

public class MadHandlerFactory extends HandlerFactory {
    private GameServer gameServer;
    private PacketFactory packetFactory;

    public MadHandlerFactory(GameServer gameServer, PacketFactory packetFactory) {
        this.gameServer = gameServer;
        this.packetFactory = packetFactory;
    }

    @Override
    public MessageToMessageDecoder newHandler() {
        return new PacketHandler(gameServer, packetFactory);
    }

    @Override
    public ByteToMessageDecoder newDecoder() {
        return new JsonObjectDecoder();
    }
}
