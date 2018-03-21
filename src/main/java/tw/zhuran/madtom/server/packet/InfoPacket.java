package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.Info;

public class InfoPacket extends MadPacket<Info> {
    private Info info;

    public InfoPacket(Info info) {
        super(PacketType.INFO, info);
    }
}
