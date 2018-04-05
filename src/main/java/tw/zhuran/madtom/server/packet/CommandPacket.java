package tw.zhuran.madtom.server.packet;

import tw.zhuran.madtom.event.CommandEvent;

public class CommandPacket extends MadPacket<CommandEvent> {
    public CommandPacket(CommandEvent content) {
        super(PacketType.EVENT, content);
    }
}
