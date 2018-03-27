package tw.zhuran.madtom.util;

import com.github.underscore.$;
import com.github.underscore.Function1;

public class NaturalTurner extends BasicTurner<Integer> {
    public NaturalTurner(int count) {
        super($.map($.range(1, count + 1), new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x;
            }
        }));
    }
}
