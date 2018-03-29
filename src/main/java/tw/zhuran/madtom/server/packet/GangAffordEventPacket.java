package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.GangAffordEvent;

public class GangAffordEventPacket extends MadPacket<GangAffordEvent> {
    public GangAffordEventPacket(GangAffordEvent content) {
        super(PacketType.EVENT, content);
    }
}
