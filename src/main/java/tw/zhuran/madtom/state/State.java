package tw.zhuran.madtom.state;

public abstract class State<A, T> {
    abstract T type();
    abstract T perform(A action);
    void begin(A action){}
    void end(A action) {}
    boolean instant() {return false;}
}
