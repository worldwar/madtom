package tw.zhuran.madtom.server;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import tw.zhuran.madtom.event.EventType;
import tw.zhuran.madtom.server.common.Packet;
import tw.zhuran.madtom.server.common.PacketFactory;
import tw.zhuran.madtom.server.packet.*;

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
                EventPacket eventPacket = JSON.parseObject(json, EventPacket.class);
                if (eventPacket.getContent().getEventType() == EventType.INTERCEPT) {
                    return JSON.parseObject(json, InterceptPacket.class);
                }
                if (eventPacket.getContent().getEventType() == EventType.COMMAND) {
                    return JSON.parseObject(json, CommandPacket.class);
                }
                if (eventPacket.getContent().getEventType() == EventType.DISPATCH) {
                    return JSON.parseObject(json, DispatchEventPacket.class);
                }
                if (eventPacket.getContent().getEventType() == EventType.GANG_AFFOARD) {
                    return JSON.parseObject(json, GangAffordEventPacket.class);
                }
                return eventPacket;
            case INFO:
                return JSON.parseObject(json, InfoPacket.class);
        }
        return madPacket;
    }
}
