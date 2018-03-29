package tw.zhuran.madtom.event;

import com.github.underscore.$;
import com.github.underscore.Function1;
import tw.zhuran.madtom.util.F;

import java.util.ArrayList;
import java.util.List;

public class InterceptEvent extends Event {
    private List<InterceptType> intercepts;

    public InterceptEvent(int player) {
        super(EventType.INTERCEPT, player);
        this.intercepts = new ArrayList<>();
    }

    public List<InterceptType> getIntercepts() {
        return intercepts;
    }

    public InterceptEvent setIntercepts(List<InterceptType> intercepts) {
        this.intercepts = intercepts;
        return this;
    }

    public void add(InterceptType type) {
        intercepts.add(type);
    }

    @Override
    public String toString() {
        List<String> names = $.map(intercepts, new Function1<InterceptType, String>() {
            @Override
            public String apply(InterceptType intercept) {
                return "可以" + intercept.toString();
            }
        });
        return F.string(names, ",");
    }
}
