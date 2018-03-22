package tw.zhuran.madtom.server;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import tw.zhuran.madtom.server.common.Packet;
import tw.zhuran.madtom.server.common.PacketFactory;
import tw.zhuran.madtom.server.packet.InfoPacket;
import tw.zhuran.madtom.server.packet.MadPacket;

public class MadPacketFactory extends PacketFactory {
    @Override
    public Packet packet(ByteBuf byteBuf) {
        String json = byteBuf.toString(Charsets.UTF_8);
        return packet(json);
    }

    public Packet packet(String json) {
        MadPacket madPacket = JSON.parseObject(json, MadPacket.class);
        switch (madPacket.getType()) {
            case EVENT:
                return JSON.parseObject(json, EventPacket.class);
            case INFO:
                return JSON.parseObject(json, InfoPacket.class);
        }
        return madPacket;
    }
}
