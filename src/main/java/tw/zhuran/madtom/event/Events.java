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

    public static Event dispatch(int player, Piece piece) {
        return new DispatchEvent(player, piece);
    }

    public static Event gangAfford(int player, Piece piece) {
        return new GangAffordEvent(player, piece);
    }

    public static Event parse(String command, int player, Piece waitPiece, Hand hand) {
        ArrayList<String> parts = Lists.newArrayList(Splitter.on(" ").split(command));
        switch (parts.get(0)) {
            case "discard":
                Optional<Piece> o = $.find(Pieces.ALL, piece -> piece.toString().equals(parts.get(1)));
                if (o.isPresent()) {
                    return Events.action(player, Actions.discard(o.get()));
                } else {
                    return null;
                }

            case "confirm":
                Piece piece = waitPiece;
                if (piece == null) {
                    return null;
                }
                switch (parts.get(1)) {
                    case "peng":
                        return Events.action(player, Actions.peng(piece));
                    case "chi":
                        return Events.action(player,
                                Actions.chi(piece, Pieces.sequence(piece, Integer.valueOf(parts.get(2)))));
                    case "gang":
                        return Events.action(player, Actions.gang(piece));
                    case "win":
                        return Events.win(player);
                }
            case "pass":
                return Events.pass(player);
            case "gang":
                piece = Pieces.find(parts.get(1));
                if (piece == null) {
                    return null;
                }

                Action action = null;
                if (piece.equals(Pieces.HONGZHONG)) {
                    action = Actions.hongzhongGang();
                } else {
                    if (Pieces.count(hand.pieces(), piece) == 4) {
                        action = Actions.angang(piece);
                    } else if (hand.getWildcards().contains(piece)) {
                        action = Actions.laiziGang(piece);
                    }
                }
                if (action != null) {
                    return Events.action(player, action);
                }
        }
        return null;
    }

    public static Event parse(String command, Board board) {
        try {
            int turn = board.turn();
            ArrayList<String> parts = Lists.newArrayList(Splitter.on(" ").split(command));
            switch (parts.get(0)) {
                case "win":
                    if (board.currentWinnable()) {
                        return Events.win(board.turn());
                    } else {
                        return null;
                    }
                case "discard":
                    Optional<Piece> o = $.find(Pieces.ALL, piece -> piece.toString().equals(parts.get(1)));
                    if (o.isPresent()) {
                        return Events.action(turn, Actions.discard(o.get()));
                    } else {
                        return null;
                    }
                case "confirm":
                    Piece piece = board.waitPiece();
                    if (piece == null) {
                        return null;
                    }
                    switch (parts.get(1)) {
                        case "peng":
                            return Events.action(Integer.valueOf(parts.get(2)), Actions.peng(piece));
                        case "chi":
                            return Events.action(Integer.valueOf(parts.get(2)),
                                    Actions.chi(piece, Pieces.sequence(piece, Integer.valueOf(parts.get(3)))));
                        case "gang":
                            return Events.action(Integer.valueOf(parts.get(2)), Actions.gang(piece));
                        case "win":
                            return Events.win(Integer.valueOf(parts.get(2)));
                    }
                case "pass":
                    return Events.pass(Integer.valueOf(parts.get(1)));

                case "gang":
                    piece = Pieces.find(parts.get(1));
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

    public String name(InterceptType type) {
        switch (type) {
            case WIN:
                return "赢";
            case GANG:
                return "杠";
            case PENG:
                return "碰";
            case CHI:
                return "吃";
        }
        return "";
    }
}
