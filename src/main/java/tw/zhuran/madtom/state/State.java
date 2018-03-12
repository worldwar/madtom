package tw.zhuran.madtom.state;

public interface State<A, T> {
    T type();
    T perform(A action);
}
