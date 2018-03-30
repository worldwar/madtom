package tw.zhuran.madtom.server;

import com.github.underscore.$;
import com.github.underscore.Block;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.server.common.Connection;
import tw.zhuran.madtom.server.common.GameContext;
import tw.zhuran.madtom.server.common.hub.Hubable;
import tw.zhuran.madtom.server.packet.InfoPacket;
import tw.zhuran.madtom.util.F;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MadGameContext extends GameContext implements Observer {
    private Board board;
    private Map<Integer, Connection> players;

    public MadGameContext(long id, Hubable hubable) {
        super(id, hubable);
        board = new Board(4);
        board.shuffle();
        board.register(this);
        players = F.index(hubable.all());
    }

    @Override
    public void start() {
        final int dealer = board.dealer();
        final MadGameContext that = this;
        $.each(players.entrySet(), new Block<Map.Entry<Integer, Connection>>() {
            @Override
            public void apply(Map.Entry<Integer, Connection> player) {
                that.notify(Packets.start(player.getKey(), dealer), player.getValue());
            }
        });
        $.each(players.entrySet(), new Block<Map.Entry<Integer, Connection>>() {
            @Override
            public void apply(Map.Entry<Integer, Connection> player) {
                that.notify(new InfoPacket(board.info(player.getKey())), player.getValue());
            }
        });
    }

    public void perform(Event event) {
        board.perform(event);
    }

    @Override
    public void update(Observable o, Object arg) {
        Event event = (Event) arg;
        for (Map.Entry<Integer, Connection> player : players.entrySet()) {
            EventPacket packet = Packets.serverEvent(event, player.getKey());
            if (packet != null) {
                notify(packet, player.getValue());
            }
        }
    }
}
