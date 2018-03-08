package tw.zhuran.madtom.domain;

import java.util.List;

public class Wall {
    private List<Pillar> pillars;
    private boolean cutted;
    private int start;
    private int end;
    private int length;

    public Wall(List<Pillar> pillars) {
        this.pillars = pillars;
        this.cutted = false;
        length = pillars.size();
        start = 0;
        end = length  - 1;
    }

    public void cut(int index) {
        cutted = true;
        start = index;
        end = start - 1;
    }

    public boolean affordable() {
        return start().notEmpty();
    }

    public Piece afford() {
        Pillar pillar = start();
        Piece piece = pillar.afford();
        if (pillar.empty()) {
            forward();
        }
        return piece;
    }

    private void forward() {
        if (start < length - 1 && start < end - 1) {
            start++;
        }
    }

    public Piece gangAfford(int point) {
        int index = end - (point - 1);
        Pillar pillar = pillars.get(index);
        Piece piece = pillar.afford();
        if (pillar.empty()) {
            if (index == end) {
                tailForward();
            } else {
                pack(index);
            }
        }
        return piece;
    }

    public boolean gangAffordable(int point) {
        return end - (point - 1) >= 0;
    }

    private Pillar start() {
        return pillars.get(start);
    }

    private Pillar end() {
        return pillars.get(end);
    }

    public boolean gangAffordable() {
        return end().notEmpty();
    }

    private void pack(int index) {
        for (int i = index; i < end; i++) {
            pillars.get(i).suck(pillars.get(i + 1));
        }

        tailForward();
    }

    private void tailForward() {
        if (tailForwardable()) {
            end--;
        }
    }

    private boolean tailForwardable() {
        return (end > 0 && end > start + 1);
    }

    public int tailLength() {
        if (gangAffordable()) {
            if (cutted) {
                return end + 1;
            } else {
                return end - start + 1;
            }
        } else {
            return 0;
        }
    }

    public Pillar advance() {
        if (gangAffordable()) {
            Pillar pillar = pillars.get(0).suck();
            pack(0);
            return pillar;
        } else {
            return null;
        }
    }

    public Pillar pillar(int index) {
        return pillars.get(index);
    }

    public int length() {
        return length;
    }
}
