package tw.zhuran.madtom.util;

import com.github.underscore.$;
import com.github.underscore.Function1;

public class ReverseNaturalTurner extends ReverseTurner<Integer> {
    public ReverseNaturalTurner(int count) {
        super($.map($.range(1, count + 1), new Function1<Integer, Object>() {
            @Override
            public Object apply(Integer x) {
                return x;
            }
        }));
    }
}
