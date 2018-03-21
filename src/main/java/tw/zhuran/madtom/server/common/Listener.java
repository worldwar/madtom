package tw.zhuran.madtom.server.common;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Listener {
    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    private HandlerFactory handlerFactory;

    public Listener(HandlerFactory factory) {
        this.handlerFactory = factory;
    }

    public ChannelFuture listen(int port) throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap()
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(handlerFactory.newDecoder()).addLast(handlerFactory.newHandler());
                    }
                })
                .group(bossGroup, workerGroup)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 128);
        return server.bind(port).sync();
    }

    public void shutdownGracefully() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
