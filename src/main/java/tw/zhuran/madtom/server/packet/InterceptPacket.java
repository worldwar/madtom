package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.InterceptEvent;
import tw.zhuran.madtom.server.EventPacket;

public class InterceptPacket extends EventPacket {
    public InterceptPacket(InterceptEvent content) {
        super(content);
    }
}
