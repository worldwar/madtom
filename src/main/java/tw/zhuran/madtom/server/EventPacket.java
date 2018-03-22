package tw.zhuran.madtom.server;

import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.server.packet.MadPacket;
import tw.zhuran.madtom.server.packet.PacketType;

public class EventPacket extends MadPacket<Event> {
    public EventPacket(Event content) {
        super(PacketType.EVENT, content);
    }
}
