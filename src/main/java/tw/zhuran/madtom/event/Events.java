package tw.zhuran.madtom.event;

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.*;

import java.util.ArrayList;

public class Events {
    public static Event win(int player) {
        return new Event(EventType.WIN, null, player);
    }
    public static Event pass(int player) {
        return new Event(EventType.PASS, null, player);
    }
    public static Event action(int player, Action action) {
        return new Event(EventType.ACTION, action, player);
    }

    public static Event parse(String command, Board board) {
        try {
            int turn = board.turn();

            ArrayList<String> parts = Lists.newArrayList(Splitter.on(" ").split(command));
            switch (parts.get(0)) {
                case "discard":
                    Optional<Piece> o = $.find(Pieces.ALL, piece -> piece.toString().equals(parts.get(1)));
                    if (o.isPresent()) {
                        return Events.action(turn, Actions.discard(o.get()));
                    } else {
                        return null;
                    }
                case "confirm":
                    switch (parts.get(1)) {
                        case "peng":
                            return Events.action(Integer.valueOf(parts.get(2)), Actions.peng(Pieces.YIWAN));
                        case "chi":
                            return Events.action(Integer.valueOf(parts.get(2)),
                                    Actions.chi(Pieces.YIWAN, Pieces.sequence(Pieces.WUWAN, Integer.valueOf(parts.get(3)))));
                        case "gang":
                            return Events.action(Integer.valueOf(parts.get(2)), Actions.gang(Pieces.YIWAN));
                        case "win":
                            return Events.win(Integer.valueOf(parts.get(2)));
                    }
                case "pass":
                    return Events.pass(Integer.valueOf(parts.get(1)));

                case "gang":
                    Piece piece = Pieces.find(parts.get(1));
                    if (piece == null) {
                        return null;
                    }

                    Action action = null;
                    Trunk trunk = board.trunk();
                    Hand hand = trunk.getHand();
                    if (piece.equals(Pieces.HONGZHONG)) {
                        action = Actions.hongzhongGang();
                    } else {
                        if (Pieces.count(hand.pieces(), piece) == 4) {
                            action = Actions.angang(piece);
                        } else if (trunk.xugangable(piece)) {
                            action = Actions.xugang(piece);
                        } else if (hand.getWildcards().contains(piece)) {
                            action = Actions.laiziGang(piece);
                        }
                    }
                    if (action != null) {
                        return Events.action(board.turn(), action);
                    }
            }
            return null;
        } catch (Exception o) {
            o.printStackTrace();
            return null;
        }
    }
}
