package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.InterceptEvent;

public class InterceptPacket extends MadPacket<InterceptEvent> {
    public InterceptPacket(InterceptEvent content) {
        super(PacketType.EVENT, content);
    }
}
