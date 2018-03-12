package tw.zhuran.madtom.state;

public abstract class AbstractState<O, A, T> implements State<A, T> {
    protected O owner;
    protected T type;
    public AbstractState(O owner, T type) {
        this.owner = owner;
        this.type = type;
    }

    @Override
    public T type() {
        return type;
    }
}
