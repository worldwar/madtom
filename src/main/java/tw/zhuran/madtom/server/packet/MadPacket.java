package tw.zhuran.madtom.server.packet;

import com.alibaba.fastjson.JSON;
import tw.zhuran.madtom.server.common.Packet;

public class MadPacket<T> implements Packet {

    protected PacketType type;

    protected T content;

    public MadPacket(PacketType type, T content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public byte[] bytes() {
        return JSON.toJSONString(this).getBytes();
    }
}
