package tw.zhuran.madtom.server.common;

import io.netty.buffer.ByteBuf;

public abstract class PacketFactory {
    public abstract Packet packet(ByteBuf byteBuf);
}
