package tw.zhuran.madtom.domain;

import java.util.HashMap;
import java.util.Map;

public class Score {
    private Map<Integer, Integer> scores;

    public Score() {
        scores = new HashMap<>();
    }

    public void put(int player, int score) {
        scores.put(player, score);
    }
}
