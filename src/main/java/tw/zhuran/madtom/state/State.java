package tw.zhuran.madtom.state;

public interface State<A, T> {
    T type();
    T perform(A action);
    default void begin(A action) {}
    default void end(A action) {}
    default boolean instant() {return false;}
}
