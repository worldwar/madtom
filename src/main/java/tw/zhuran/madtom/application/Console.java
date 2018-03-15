package tw.zhuran.madtom.application;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.domain.Trunk;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.Events;

import java.util.ArrayList;
import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        Board board = new Board(4);
        board.shuffle();
        while (true) {
            System.out.print(board);
            Scanner scanner = new Scanner(System.in);
            System.out.print(">");
            String command = scanner.nextLine();
            ArrayList<String> commands = Lists.newArrayList(Splitter.on(" ").split(command));
            String c = commands.get(0);

            switch (c) {
                case "shuffle":
                    board.shuffle();
                    break;
                case "print":
                    if (commands.size() > 1) {
                        Integer player = Integer.valueOf(commands.get(1));
                        Trunk trunk = board.trunk(player);
                        System.out.println(trunk.toString());
                    } else {
                        System.out.println(board);
                    }
                    break;
                case "exit":
                    return;
                default:
                    Event event = Events.parse(command, board);
                    if (event == null) {
                        System.out.println("bad command!\n");
                        break;
                    }
                    board.perform(event);
            }
        }
    }

}
