package tw.zhuran.madtom.state;

import com.github.underscore.$;
import com.github.underscore.Predicate;

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
        if (current.type() == type) {
            return;
        }
        current = state(type);
        if (current.instant()) {
            perform(action);
        }
        current.begin(action);
    }

    protected X state(final T s) {
        List<X> result = $.filter(states, new Predicate<X>() {
            @Override
            public Boolean apply(X state) {
                return state.type() == s;
            }
        });
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

    @Override
    public String toString() {
        return current.toString();
    }
}
