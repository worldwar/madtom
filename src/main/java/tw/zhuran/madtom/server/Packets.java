package tw.zhuran.madtom.server;

import tw.zhuran.madtom.server.packet.StartPacket;

import java.util.HashMap;
import java.util.Map;

public class Packets {
    public static StartPacket start(int player, int dealer) {
        Map<String, Integer> map = new HashMap<>();
        map.put("self", player);
        map.put("dealer", dealer);
        return new StartPacket(map);
    }
}
