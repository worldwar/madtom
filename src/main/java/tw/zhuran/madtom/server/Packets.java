package tw.zhuran.madtom.server;

import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.DispatchEvent;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.GangAffordEvent;
import tw.zhuran.madtom.event.InterceptEvent;
import tw.zhuran.madtom.server.packet.InterceptPacket;
import tw.zhuran.madtom.server.packet.MadPacket;
import tw.zhuran.madtom.server.packet.PacketType;
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

    public static EventPacket event(Event event, int receiver) {
        switch (event.getEventType()) {
            case DISPATCH:
                return dispatch(event, receiver);
            case GANG_AFFOARD:
                return gangAfford(event, receiver);
            case ACTION:
                return action(event, receiver);
            case INTERCEPT:
                return new InterceptPacket((InterceptEvent) event);
            default:
                return new EventPacket(event);
        }
    }

    private static EventPacket action(Event event, int receiver) {
        Action action = event.getAction();
        if (action.getType() == ActionType.ANGANG && event.getPlayer() != receiver) {
            Event duplicate = event.duplicate();
            duplicate.setAction(Actions.angang(Pieces.HONGZHONG));
            return new EventPacket(duplicate);
        }
        return new EventPacket(event);
    }

    private static EventPacket gangAfford(Event event, int receiver) {
        if (event.getPlayer() == receiver) {
            return new EventPacket(event);
        } else {
            GangAffordEvent dispatchEvent = ((GangAffordEvent) event).duplicate();
            dispatchEvent.setPiece(Pieces.HONGZHONG);
            return new EventPacket(dispatchEvent);
        }
    }

    private static EventPacket dispatch(Event event, int receiver) {
        if (event.getPlayer() == receiver) {
            return new EventPacket(event);
        } else {
            DispatchEvent dispatchEvent = ((DispatchEvent) event).duplicate();
            dispatchEvent.setPiece(Pieces.HONGZHONG);
            return new EventPacket(dispatchEvent);
        }
    }

    public static MadPacket ready() {
        return new MadPacket(PacketType.READY, null);
    }

    public static MadPacket unready() {
        return new MadPacket(PacketType.UNREADY, null);
    }
}
