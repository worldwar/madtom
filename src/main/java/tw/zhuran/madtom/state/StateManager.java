package tw.zhuran.madtom.state;

import com.github.underscore.$;

import java.util.ArrayList;
import java.util.List;

public class StateManager<A, T, X extends State<A, T>> {
    private List<X> states = new ArrayList<>();
    private X current;

    public StateManager(List<X> states) {
        this.states = states;
    }

    public void init(T type) {
        current = state(type);
    }

    public void perform(A action) {
        T type = current.perform(action);
        current = state(type);
    }

    private X state(T s) {
        List<X> result = $.filter(states, state -> state.type() == s);
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public void setState(T type) {
        current = state(type);
    }

    public T currentState() {
        return current.type();
    }
}
