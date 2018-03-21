package tw.zhuran.madtom.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketHandler extends ByteToMessageDecoder {
    private GameServer gameServer;
    private PacketFactory packetFactory;

    public PacketHandler(GameServer gameServer, PacketFactory gameFactory) {
        this.gameServer = gameServer;
        this.packetFactory = gameFactory;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        gameServer.come(new Connection(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        gameServer.go(ctx.hashCode());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Packet packet = packetFactory.packet(in);
        process(packet, ctx, in);
    }

    private void process(Packet packet, ChannelHandlerContext ctx, ByteBuf in) {
        long id = ctx.hashCode();
        gameServer.dispatch(id, packet);
    }

    public PacketHandler duplicate() {
        return new PacketHandler(gameServer, packetFactory);
    }
}
