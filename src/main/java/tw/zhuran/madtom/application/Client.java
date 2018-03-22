package tw.zhuran.madtom.application;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.Hand;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.Events;
import tw.zhuran.madtom.event.Info;
import tw.zhuran.madtom.server.EventPacket;
import tw.zhuran.madtom.server.MadPacketFactory;
import tw.zhuran.madtom.server.Packets;
import tw.zhuran.madtom.server.common.Packet;
import tw.zhuran.madtom.server.packet.InfoPacket;
import tw.zhuran.madtom.server.packet.MadPacket;
import tw.zhuran.madtom.util.F;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static tw.zhuran.madtom.event.EventType.ACTION;

public class Client {
    public static Client instance = new Client();
    public Hand hand;
    public Piece waitPiece;
    public int player;
    public Piece wildcard;

    public static void main(String[] args) throws InterruptedException {
        Connector connector = new Connector();
        connector.setClient(instance);
        connector.connect("localhost", 32211);
        connector.send(Packets.ready());
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">");
            String command = scanner.nextLine();
            ArrayList<String> commands = Lists.newArrayList(Splitter.on(" ").split(command));
            String c = commands.get(0);

            switch (c) {
                case "ready":
                    connector.send(Packets.ready());
                    break;
                case "print":
                    System.out.println(instance.toString());
                    break;
                case "exit":
                    return;
                default:
                    Event event = Events.parse(command, instance.player, instance.waitPiece, instance.hand);
                    if (event == null) {
                        System.out.println("bad command!\n");
                        break;
                    }
                    connector.send(Packets.event(event, instance.player));
            }
        }
    }

    public void handle(MadPacket madPacket) {
        switch (madPacket.getType()) {
            case EVENT:
                EventPacket eventPacket = (EventPacket) madPacket;
                Event content = eventPacket.getContent();
                if (content.getEventType() == ACTION) {
                    Action action = content.getAction();
                    if (action.getType() == ActionType.DISCARD) {
                        waitPiece = action.getPiece();
                    }
                }
                break;
            case INFO:
                InfoPacket infoPacket = (InfoPacket) madPacket;
                Info info = infoPacket.getContent();
                hand = construct(info);
                player = info.getSelf();
                wildcard = info.getWildcard();
                break;
        }
    }

    public static Hand construct(Info info) {
        Hand hand = new Hand();
        hand.setWildcard(info.getWildcard());
        List<Piece> pieces = info.getPieces();
        pieces.forEach(hand::feed);
        return hand;
    }

    @Override
    public String toString() {
        List<Piece> all = hand.all();
        StringBuilder builder = new StringBuilder();
        builder.append("癞子: " + wildcard.toString() + "\n");
        builder.append("手牌: " + F.string(all, ""));
        return builder.toString();
    }
}
class Connector {
    private EventLoopGroup group;
    private Bootstrap bootstrap = null;
    private Channel channel;
    private Client client;

    public void init() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new JsonObjectDecoder()).addLast(new ClientHandler(client));
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ChannelFuture connect(String address, int port) throws InterruptedException {
        if (bootstrap == null) {
            init();
        }
        ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
        channel = channelFuture.channel();
        return channelFuture;
    }

    public void send(MadPacket madPacket) {
        byte[] bytes = madPacket.bytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        channel.writeAndFlush(byteBuf);
    }

    public void shutdownGracefully() {
        group.shutdownGracefully();
        bootstrap = null;
    }
}

class ClientHandler extends MessageToMessageDecoder<ByteBuf> {

    private Client client;

    public ClientHandler(Client client) {
        this.client = client;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String s = in.toString(Charsets.UTF_8);
        System.out.println(s);
        Packet packet = new MadPacketFactory().packet(s);
        client.handle((MadPacket) packet);
    }
}