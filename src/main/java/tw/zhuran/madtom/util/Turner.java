package tw.zhuran.madtom.util;

public interface Turner<T> {
    T current();
    T next();
    T previous();
    void turnTo(T t);
    void turnNext();
}
