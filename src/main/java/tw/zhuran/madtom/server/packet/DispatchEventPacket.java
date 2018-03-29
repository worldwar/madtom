package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.DispatchEvent;

public class DispatchEventPacket extends MadPacket<DispatchEvent> {
    public DispatchEventPacket(DispatchEvent content) {
        super(PacketType.EVENT, content);
    }
}
