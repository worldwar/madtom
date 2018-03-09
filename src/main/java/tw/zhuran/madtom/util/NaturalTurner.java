package tw.zhuran.madtom.util;

import com.github.underscore.$;

public class NaturalTurner extends BasicTurner<Integer> {
    public NaturalTurner(int count) {
        super($.map($.range(1, count + 1), x -> x));
    }
}
