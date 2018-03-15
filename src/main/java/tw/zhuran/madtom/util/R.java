package tw.zhuran.madtom.util;

import java.util.Random;

public class R {
    private static Random random = new Random();

    public static int number(int min, int max) {
        if (min > max) return number(max, min);
        return random.nextInt(max - min + 1) + min;
    }

    public static int dice() {
        return number(1, 6);
    }

    public static int dealer() {
        return number(1, 4);
    }
}
