package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.util.F;

import java.util.*;

public class Pieces {
    public static Piece YIWAN = new Piece(Kind.WAN, 1);
    public static Piece ERWAN = new Piece(Kind.WAN, 2);
    public static Piece SANWAN = new Piece(Kind.WAN, 3);
    public static Piece SIWAN = new Piece(Kind.WAN, 4);
    public static Piece WUWAN = new Piece(Kind.WAN, 5);
    public static Piece LIUWAN = new Piece(Kind.WAN, 6);
    public static Piece QIWAN = new Piece(Kind.WAN, 7);
    public static Piece BAWAN = new Piece(Kind.WAN, 8);
    public static Piece JIUWAN = new Piece(Kind.WAN, 9);
    public static Piece YITIAO = new Piece(Kind.TIAO, 1);
    public static Piece ERTIAO = new Piece(Kind.TIAO, 2);
    public static Piece SANTIAO = new Piece(Kind.TIAO, 3);
    public static Piece SITIAO = new Piece(Kind.TIAO, 4);
    public static Piece WUTIAO = new Piece(Kind.TIAO, 5);
    public static Piece LIUTIAO = new Piece(Kind.TIAO, 6);
    public static Piece QITIAO = new Piece(Kind.TIAO, 7);
    public static Piece BATIAO = new Piece(Kind.TIAO, 8);
    public static Piece JIUTIAO = new Piece(Kind.TIAO, 9);
    public static Piece YITONG = new Piece(Kind.TONG, 1);
    public static Piece ERTONG = new Piece(Kind.TONG, 2);
    public static Piece SANTONG = new Piece(Kind.TONG, 3);
    public static Piece SITONG = new Piece(Kind.TONG, 4);
    public static Piece WUTONG = new Piece(Kind.TONG, 5);
    public static Piece LIUTONG = new Piece(Kind.TONG, 6);
    public static Piece QITONG = new Piece(Kind.TONG, 7);
    public static Piece BATONG = new Piece(Kind.TONG, 8);
    public static Piece JIUTONG = new Piece(Kind.TONG, 9);
    public static Piece DONGFENG = new Piece(Kind.FENG, 1);
    public static Piece NANFENG = new Piece(Kind.FENG, 2);
    public static Piece XIFENG = new Piece(Kind.FENG, 3);
    public static Piece BEIFENG = new Piece(Kind.FENG, 4);
    public static Piece HONGZHONG = new Piece(Kind.FENG, 5);
    public static Piece FACAI = new Piece(Kind.FENG, 6);
    public static Piece BAIBAN = new Piece(Kind.FENG, 7);

    public static List<Piece> WAN = Lists.newArrayList(YIWAN, ERWAN, SANWAN, SIWAN, WUWAN, LIUWAN, QIWAN, BAWAN, JIUWAN);
    public static List<Piece> TIAO = Lists.newArrayList(YITIAO, ERTIAO, SANTIAO, SITIAO, WUTIAO, LIUTIAO, QITIAO, BATIAO, JIUTIAO);
    public static List<Piece> TONG = Lists.newArrayList(YITONG, ERTONG, SANTONG, SITONG, WUTONG, LIUTONG, QITONG, BATONG, JIUTONG);
    public static List<Piece> FENG = Lists.newArrayList(DONGFENG, NANFENG, XIFENG, BEIFENG, HONGZHONG, FACAI, BAIBAN);
    public static EnumMap<Kind, List<Piece>> SUITS = new EnumMap<>(Kind.class);

    public static List<Piece> ALL = new ArrayList<>();

    static {
        ALL.addAll(WAN);
        ALL.addAll(TIAO);
        ALL.addAll(TONG);
        ALL.addAll(FENG);

        SUITS.put(Kind.WAN, WAN);
        SUITS.put(Kind.TIAO, TIAO);
        SUITS.put(Kind.TONG, TONG);
        SUITS.put(Kind.FENG, FENG);
    }

    public static Piece piece(Kind kind, int index) {
        return suit(kind).get(index);
    }

    public static List<Piece> deck() {
        return $.chain(ALL).map(piece -> F.repeat(piece, 4)).flatten().value();
    }

    private boolean paired(List<Piece> pieces) {
        return pieces.size() == 2 && pieces.get(0).equals(pieces.get(1));
    }

    public boolean sentenced(List<Piece> pieces) {
        return tripled(pieces) || sequenced(pieces);
    }

    private boolean sequenced(List<Piece> pieces) {
        if (pieces.size() == 3) {
            List<Piece> orderedPieces = order(pieces);
            return following(orderedPieces.get(0), orderedPieces.get(1)) &&
                    following(orderedPieces.get(1), orderedPieces.get(2));
        }
        return false;
    }

    public static boolean well(final List<Piece> pieces) {
        if (pieces.size() == 0) {
            return true;
        }
        List<Group> sentences = headSentences(pieces);

        for (Group sentence : sentences) {
            List<Piece> remains = subtract(pieces, sentence.getPieces());
            if (well(remains)) {
                return true;
            }
        }
        return false;
    }

    public static List<Piece> add(final List<Piece> pieces, Piece piece) {
        ArrayList<Piece> copies = Lists.newArrayList(pieces);
        copies.add(piece);
        return copies;
    }

    public static List<Piece> subtract(final List<Piece> pieces, List<Piece> sentence) {
        ArrayList<Piece> copies = Lists.newArrayList(pieces);
        exclude(copies, sentence);
        return copies;
    }

    public static List<Piece> subtract(final List<Piece> pieces, Piece piece) {
        return subtract(pieces, Lists.newArrayList(piece));
    }

    public static List<Group> headSentences(List<Piece> pieces) {
        List<Group> sentences = new ArrayList<>();
        if (pieces.size() < 3) {
            return sentences;
        }
        List<Group> possibleSentences = possibleSentences(pieces.get(0));
        return $.filter(possibleSentences, (sentence) -> contains(pieces, sentence.getPieces()));
    }

    public static  List<Group> possibleSentences(Piece piece) {
        List<Group> sentences = Lists.newArrayList();
        sentences.add(triple(piece));
        sentences.addAll(possibleSequences(piece));
        return sentences;
    }

    public static  List<Group> possibleSequences(Piece piece) {
        List<Group> sentences = Lists.newArrayList();
        if (piece.getKind() != Kind.FENG) {
            for (int position : $.range(1, 4)) {
                Group sequence = sequence(piece, position);
                if (sequence != null) {
                    sentences.add(sequence);
                }
            }
        }
        return sentences;
    }

    public static List<Group> pairs(List<Piece> pieces) {
        return $.map(countLargeThan(pieces, 1), Pieces::pair);
    }

    public static List<Piece> countLargeThan(List<Piece> pieces, int count) {
        Map<Piece, List<Piece>> value =
                $.groupBy(pieces, arg -> arg);
        return $.chain(value.entrySet())
                .filter((entry) -> entry.getValue().size() > count).map(Map.Entry::getKey).value();
    }

    private boolean tripled(List<Piece> pieces) {
        return pieces.size() == 3 &&
                pieces.get(0).equals(pieces.get(1)) &&
                pieces.get(0).equals(pieces.get(2));
    }

    private List<Piece> order(List<Piece> pieces) {
        List<Piece> orderedPieces = new ArrayList<>();
        return orderedPieces;
    }

    private boolean following(Piece p, Piece q) {
        return p.getKind() == q.getKind() && p.getIndex() + 1 == q.getIndex();
    }

    public static Group sequence(Piece piece, int position) {
        List<Piece> pieces = null;
        int index = piece.getIndex();
        if (position == 1) {
            if (index < 8) {
                pieces = Lists.newArrayList(piece, piece.next(), piece.next().next());
            }
        } else if (position == 2) {
            if (index < 9 && index > 1) {
                pieces = Lists.newArrayList(piece.prev(), piece, piece.next());
            }
        } else if (position == 3) {
            if (index > 2) {
                pieces = Lists.newArrayList(piece.prev().prev(), piece.prev(), piece);
            }
        }
        if (pieces == null) {
            return null;
        }
        return new Group(pieces, GroupType.SEQUENCE, piece.getKind());
    }

    public static boolean contains(List<Piece> pieces, List<Piece> sentence) {
        ArrayList<Piece> copies = Lists.newArrayList(pieces);
        for (Piece piece : sentence) {
            boolean contains = copies.contains(piece);
            if (contains) {
                copies.remove(piece);
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean exclude(List<Piece> pieces, List<Piece> part) {
        int originalSize = pieces.size();
        $.each(part, piece -> exclude(pieces, piece));
        return pieces.size() == originalSize - part.size();
    }

    public static boolean exclude(List<Piece> pieces, Piece piece) {
        return pieces.remove(piece);
    }

    public static void excludeAll(List<Piece> pieces, Piece piece) {
        while (exclude(pieces,piece)){}
    }

    public static Group pair(Piece piece) {
        return new Group(Lists.newArrayList(piece, piece), GroupType.PAIR, piece.getKind());
    }

    public static Group triple(Piece piece) {
        return new Group(Lists.newArrayList(piece, piece, piece), GroupType.TRIPLE, piece.getKind());
    }

    public static List<Piece> suit(final List<Piece> pieces, final Kind kind) {
        return $.chain(pieces).filter(piece -> piece.getKind() == kind).sortBy(Piece::getIndex).value();
    }

    public static List<Piece> suit(final Kind kind) {
        return SUITS.get(kind);
    }


    public static void times(Runnable runnable, int count) {
        if (count != 0) {
            runnable.run();
            times(runnable, count - 1);
        }
    }

    public static void orderInsert(List<Piece> pieces, Piece piece) {
        int i = 0;
        for(Piece p : pieces) {
            if (p.getIndex() < piece.getIndex()) {
                i++;
            } else {
                break;
            }
        }
        pieces.add(i, piece);
    }

    public static List<List<Piece>> permutations(int count, List<Piece> pool, List<List<Piece>> middle) {
        if (count == 0) {
            return middle;
        }

        List<List<Piece>> result = Lists.newArrayList();
        for (List<Piece> permutation : middle) {
            for (Piece piece : pool) {
                result.add(add(permutation, piece));
            }
        }
        return permutations(count -1, pool, result);
    }

    public static List<List<Piece>> permutations(int count, List<Piece> pool) {
        return permutations(count, pool, F.repeat(Lists.newArrayList(), 1));
    }

    public static List<List<Piece>> permutations(int count) {
        return permutations(count, Pieces.ALL);
    }

    public static List<List<Piece>> combinations(int count, List<Piece> pool, List<List<Piece>> middle) {
        if (count == 0) {
            return middle;
        }

        List<List<Piece>> result = Lists.newArrayList();
        int index = 0;
        for (Piece piece : pool) {
            for (List<Piece> permutation : middle) {
                if (permutation.size() == 0 || pool.indexOf(permutation.get(permutation.size() - 1)) <= index) {
                    result.add(add(permutation, piece));
                }
            }
            index++;
        }
        return combinations(count -1, pool, result);
    }

    public static List<List<Piece>> combinations(int count, List<Piece> pool) {
        return combinations(count, pool, F.repeat(Lists.newArrayList(), 1));
    }

    public static List<List<Piece>> combinations(int count) {
        return combinations(count, Pieces.ALL);
    }

    public static List<Piece> partners(Piece piece) {
        return $.chain(possibleSentences(piece)).map(group -> group.getPieces()).flatten().uniq().value();
    }

    public static List<List<Piece>> uniqueCombinations(int i, List<Piece> pool) {
        return uniqueCombinations(i, Lists.newArrayList(pool), Lists.newArrayList());
    }

    public static List<List<Piece>> uniqueCombinations(int i, List<Piece> pool, List<Piece> base) {
        if (i == 0) {
            List<List<Piece>> r = new ArrayList<>();
            r.add(base);
            return r;
        }
        if (pool.size() == 0) {
            return Lists.newArrayList();
        }
        List<Piece> uniquePool = $.uniq(pool);
        List<List<Piece>> result = Lists.newArrayList();
        for(Piece piece : uniquePool) {
            result.addAll(uniqueCombinations(i - 1, Pieces.subtract(pool, piece), add(base, piece)));
            excludeAll(pool, piece);
        }
        return result;
    }

    public static int count(List<Piece> pieces, Piece piece) {
        int count = 0;
        for (Piece p : pieces) {
            if (p.equals(piece)) {
                count++;
            }
        }
        return count;
    }

    public static boolean matchTripleGroup(Action action) {
        Group group = action.getGroup();
        return group != null && group.getGroupType() == GroupType.TRIPLE;
    }


    public static boolean matchTripleGroup(Group group) {
        return group != null && group.getGroupType() == GroupType.TRIPLE;
    }

    public static boolean hasGroup(Action action) {
        return action.getGroup() != null;
    }

    public static boolean sameKind(List<Group> groups) {
        if (groups.size() == 0) {
            return true;
        }
        Group first = groups.get(0);
        return $.all(groups, group -> group.getKind() == first.getKind());
    }

    public static boolean isJiang(Piece piece) {
        return piece.getKind() != Kind.FENG && (piece.getIndex() == 2 || piece.getIndex() == 5 || piece.getIndex() == 8);
    }

    public static boolean self(TriggerType triggerType) {
        return triggerType == TriggerType.SELF || triggerType == TriggerType.FIRE || triggerType == TriggerType.BOTTOM;
    }

    public static Piece wildcard(Piece piece) {
        Piece wildcard = piece.circularNext();
        if (wildcard == Pieces.HONGZHONG) {
            wildcard = wildcard.circularNext();
        }
        return wildcard;
    }

    public static boolean opened(Action action) {
        return hasGroup(action) && action.getType() != ActionType.ANGANG;
    }
}
