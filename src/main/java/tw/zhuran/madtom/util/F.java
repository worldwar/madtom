package tw.zhuran.madtom.util;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class F {
    public static <T> List<T> repeat(T v, int count) {
        if (count == 0) {
            return Lists.newArrayList();
        } else {
            List<T> pieces = repeat(v, count - 1);
            pieces.add(v);
            return pieces;
        }
    }

    public static <T> List<T> multiple(Supplier<T> supplier, int count) {
        List<T> result = Lists.newArrayList();
        if (count == 0) {
            return result;
        } else {
            result.add(supplier.get());
            result.addAll(multiple(supplier, count - 1));
            return result;
        }
    }

    public static <T> List<T> add(final List<T> first, final List<T> second) {
        List<T> copies = Lists.newArrayList(first);
        copies.addAll(second);
        return copies;
    }

    public static <T> Map<Integer, T> index(List<T> list) {
        int index = 1;
        Map<Integer, T> result = new HashMap<>();
        for (T v : list) {
            result.put(index, v);
            index++;
        }
        return result;
    }
}
