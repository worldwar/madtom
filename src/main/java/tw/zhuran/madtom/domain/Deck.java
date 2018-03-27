package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.github.underscore.Function1;
import com.github.underscore.FunctionAccum;
import com.google.common.base.Supplier;
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
        final List<Piece> deck = Pieces.deck();
        F.times(new Runnable() {
            @Override
            public void run() {
                Collections.shuffle(deck);
            }
        }, 10);
        List<List<Pillar>> pillarsList = $.chain(deck).chunk(2).map(new Function1<List<Piece>, Pillar>() {
            @Override
            public Pillar apply(List<Piece> list) {
                return new Pillar(list);
            }
        }).chunk(deck.size() / 2 / size)
                .value();
        walls = F.index($.map(pillarsList, new Function1<List<Pillar>, Wall>() {
            @Override
            public Wall apply(List<Pillar> pillars) {
                return new Wall(pillars);
            }
        }));
        head = new ReverseNaturalTurner(size);
        tail = new NaturalTurner(size);
    }

    public Deck(List<Piece> deck, int size) {
        List<List<Pillar>> pillarsList = $.chain(deck).chunk(2).map(new Function1<List<Piece>, Pillar>() {
            @Override
            public Pillar apply(List<Piece> list) {
                return new Pillar(list);
            }
        }).chunk(deck.size() / 2 / size)
                .value();
        walls = F.index($.map(pillarsList, new Function1<List<Pillar>, Wall>() {
            @Override
            public Wall apply(List<Pillar> pillars) {
                return new Wall(pillars);
            }
        }));
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
        return F.multiple(new Supplier<Piece>() {
            @Override
            public Piece get() {
                return Deck.this.afford();
            }
        }, 4);
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
        return $.foldl(walls.values(), new FunctionAccum<Integer, Wall>() {
            @Override
            public Integer apply(Integer a, Wall b) {
                return a + b.remainPillars();
            }
        }, 0);
    }
}
