package tw.zhuran.madtom.server.common;

public class N {
    public static void send(Connection connection, Packet packet) {
        connection.send(packet.bytes());
    }
}
