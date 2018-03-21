package tw.zhuran.madtom.server;

import com.alibaba.fastjson.JSON;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.server.common.Packet;

public class EventPacket implements Packet {
    private Event event;

    public EventPacket(Event event) {
        this.event = event;
    }

    @Override
    public byte[] bytes() {
        return JSON.toJSONString(event).getBytes();
    }
}
