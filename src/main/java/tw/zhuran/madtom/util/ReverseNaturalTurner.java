package tw.zhuran.madtom.util;

import com.github.underscore.$;

public class ReverseNaturalTurner extends ReverseTurner {
    public ReverseNaturalTurner(int count) {
        super($.map($.range(1, count + 1), x -> x));
    }
}
