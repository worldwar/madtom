package tw.zhuran.madtom.application;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.Board;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.Events;

import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        Board board = new Board(4);
        board.setDealer(2);
        board.cut(1, 1);
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(">");
            String command = scanner.nextLine();
            String c = Lists.newArrayList(Splitter.on(" ").split(command)).get(0);

            switch (c) {
                case "print":
                    System.out.println(board);
                    break;
                case "exit":
                    return;
                default:
                    Event event = Events.parse(command, board);
                    if (event == null) {
                        System.out.println("bad command!\n");
                        return;
                    }
                    board.perform(event);
            }
        }
    }

}
