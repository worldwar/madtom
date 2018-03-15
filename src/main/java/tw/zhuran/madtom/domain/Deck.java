package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.NaturalTurner;
import tw.zhuran.madtom.util.ReverseNaturalTurner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deck {
    private Map<Integer, Wall> walls = new HashMap<>();
    private ReverseNaturalTurner head;
    private NaturalTurner tail;

    public Deck(int size) {
        List<Piece> deck = Pieces.deck();
        F.times(() -> Collections.shuffle(deck), 10);
        List<List<Pillar>> pillarsList = $.chain(deck).chunk(2).map(list -> new Pillar(list)).chunk(deck.size() / 2 / size)
                .value();
        walls = F.index($.map(pillarsList, Wall::new));
        head = new ReverseNaturalTurner(size);
        tail = new NaturalTurner(size);
    }

    public Deck(List<Piece> deck, int size) {
        List<List<Pillar>> pillarsList = $.chain(deck).chunk(2).map(list -> new Pillar(list)).chunk(deck.size() / 2 / size)
                .value();
        walls = F.index($.map(pillarsList, Wall::new));
        head = new ReverseNaturalTurner(size);
        tail = new NaturalTurner(size);
    }

    public void cut(int startWall, int index) {
        Wall wall = walls.get(startWall);
        wall.cut(index);
        head.turnTo(startWall);
        tail.turnTo(startWall);
    }

    public Wall wall(Integer index) {
        return walls.get(index);
    }

    public Piece afford() {
        Wall wall = wall(head.current());
        Piece piece = wall.afford();
        if (!wall.affordable()) {
            head.turnNext();
        }
        return piece;
    }

    public List<Piece> deal() {
        return F.multiple(this::afford, 4);
    }

    public Piece gangAfford(int point) {
        Wall wall = wall(tail.current());
        if (wall.gangAffordable(point)) {
            Piece piece = wall.gangAfford(point);
            if (!wall.gangAffordable()) {
                tail.turnNext();
            }
            return piece;
        } else {
            Wall previousWall = wall(tail.next());
            Piece piece = previousWall.gangAfford(point - wall.tailLength());
            Pillar edge = previousWall.pillar(previousWall.length() - 1);
            if (edge.empty()) {
                Pillar advanced = wall.advance();
                edge.suck(advanced);
            }
            return piece;
        }
    }

    public int remainPillars() {
        return $.foldl(walls.values(), (a, b) -> a + b.remainPillars(), 0);
    }
}
