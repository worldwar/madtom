package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.Info;

public class InfoPacket extends MadPacket<Info> {
    public InfoPacket(Info content) {
        super(PacketType.INFO, content);
    }
}
