package tw.zhuran.madtom.domain;

import java.util.HashMap;
import java.util.Map;

public class Deck {
    private Map<Integer, Wall> walls = new HashMap<>();
    private int size;
    private int cuttedWall;
    private int start;
    private int end;

    public Wall wall(int index) {
        return walls.get(index);
    }

    public int next(int index) {
        if (index == 1) {
            return size;
        } else {
            return index - 1;
        }
    }

    public int previous(int index) {
        if (index == size) {
            return 1;
        } else {
            return index + 1;
        }
    }

    public void cut(int index) {
        Wall wall = walls.get(index);
        wall.cut(index);
        cuttedWall = index;
        start = index;
        end = index;
    }

    public Piece afford() {
        Wall wall = wall(start);
        Piece piece = wall.afford();
        if (wall.affordable()) {
            forward();
        }
        return piece;
    }

    public Piece gangAfford(int point) {
        Wall wall = wall(end);
        if (wall.gangAffordable(point)) {
            Piece piece = wall.gangAfford(point);
            if (!wall.gangAffordable()) {
                tailForward();
            }
            return piece;
        } else {
            Wall previousWall = wall(previous(end));
            Piece piece = previousWall.gangAfford(point - wall.tailLength());
            Pillar edge = previousWall.pillar(previousWall.length() - 1);
            if (edge.empty()) {
                Pillar advanced = wall.advance();
                edge.suck(advanced);
            }
            return piece;
        }
    }

    private void forward() {
        start = next(start);
    }

    private void tailForward() {
        end = previous(end);
    }
}
