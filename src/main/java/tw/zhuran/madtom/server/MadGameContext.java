package tw.zhuran.madtom.server;

import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.server.common.Connection;
import tw.zhuran.madtom.server.common.GameContext;
import tw.zhuran.madtom.server.common.hub.Hubable;
import tw.zhuran.madtom.server.packet.InfoPacket;
import tw.zhuran.madtom.util.F;

import java.util.Map;

public class MadGameContext extends GameContext {
    private Board board;
    private Map<Integer, Connection> players;

    public MadGameContext(long id, Hubable hubable) {
        super(id, hubable);
        board = new Board(4);
        board.shuffle();
        players = F.index(hubable.all());
    }

    @Override
    public void start() {
        int dealer = board.dealer();
        players.entrySet().forEach(player -> notify(Packets.start(player.getKey(), dealer), player.getValue()));
        players.entrySet().forEach(player -> notify(new InfoPacket(board.info(player.getKey())), player.getValue()));
    }
}
