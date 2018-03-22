package tw.zhuran.madtom.application;

import tw.zhuran.madtom.server.MadGameServer;

public class Server {
    public static void main(String[] args) {
        MadGameServer server = new MadGameServer();
        server.start(32211);
    }
}
