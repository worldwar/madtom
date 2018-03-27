package tw.zhuran.madtom.util;

import java.util.List;

public interface Turner<T> {
    T current();
    T next();
    T previous();
    void turnTo(T t);
    void turnNext();
    List<T> ordered();
}
