package tw.zhuran.madtom.server.packet;

import java.util.Map;

public class StartPacket extends MadPacket<Map<String, Integer>> {
    public StartPacket(Map<String, Integer> content) {
        super(PacketType.START, content);
    }
}
