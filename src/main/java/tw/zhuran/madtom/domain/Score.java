package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.github.underscore.Function1;
import tw.zhuran.madtom.util.F;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Score {
    private Map<Integer, Integer> scores;

    public Score() {
        scores = new HashMap<>();
    }

    public void put(int player, int score) {
        scores.put(player, score);
    }

    @Override
    public String toString() {
        Set<String> scores = $.map(this.scores.entrySet(), new Function1<Map.Entry<Integer, Integer>, String>() {
            @Override
            public String apply(Map.Entry<Integer, Integer> entry) {
                return "玩家" + entry.getKey() + " " + "得分" + entry.getValue();
            }
        });
        return F.string(scores, "\n");
    }
}
