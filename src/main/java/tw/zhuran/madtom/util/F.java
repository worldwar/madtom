package tw.zhuran.madtom.util;

import com.github.underscore.$;
import com.github.underscore.Function1;
import com.github.underscore.FunctionAccum;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import java.util.*;

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

    public static void times(Runnable runnable, int count) {
        if (count != 0) {
            runnable.run();
            times(runnable, count - 1);
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

    public static int power(int base, int p) {
        if (p == 0) {
            return 1;
        }
        return base * power(base, p - 1);
    }

    public static <T> List<T> list(T[] array) {
        return Arrays.asList(array);
    }

    public static List<Integer> list(int[] array) {
        return $.map(array, new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x;
            }
        });
    }

    public static <T> String string(List<T> list, final String splitter) {
        return $.foldl(list, new FunctionAccum<String, T>() {
            @Override
            public String apply(String a, T b) {
                return a + b + splitter;
            }
        }, "");
    }

    public static <T> String string(Set<T> list, final String splitter) {
        return $.foldl(list, new FunctionAccum<String, T>() {
            @Override
            public String apply(String a, T b) {
                return a + b + splitter;
            }
        }, "");
    }
}
