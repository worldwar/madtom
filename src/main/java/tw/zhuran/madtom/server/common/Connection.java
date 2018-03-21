package tw.zhuran.madtom.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class Connection {
    private long id;
    private ChannelHandlerContext ctx;

    public Connection(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.id = ctx.hashCode();
    }

    public void send(byte[] bytes) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        ctx.writeAndFlush(byteBuf);
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Connection that = (Connection) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}