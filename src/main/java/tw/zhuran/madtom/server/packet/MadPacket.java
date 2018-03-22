package tw.zhuran.madtom.server.packet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tw.zhuran.madtom.server.common.Packet;

public class MadPacket<T> implements Packet {

    protected PacketType type;

    protected T content;

    public MadPacket(PacketType type, T content) {
        this.type = type;
        this.content = content;
    }

    public PacketType getType() {
        return type;
    }

    public T getContent() {
        return content;
    }

    public void setType(PacketType type) {
        this.type = type;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public byte[] bytes() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNonStringKeyAsString).getBytes();
    }
}
