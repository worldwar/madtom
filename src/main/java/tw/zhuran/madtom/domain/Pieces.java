package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pieces {
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

    public static List<Piece> subtract(final List<Piece> pieces, List<Piece> sentence) {
        ArrayList<Piece> copies = Lists.newArrayList(pieces);
        exclude(copies, sentence);
        return copies;
    }

    private static List<Group> headSentences(List<Piece> pieces) {
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
        Map<Piece, List<Piece>> value =
                $.groupBy(pieces, arg -> arg);
        return $.chain(value.entrySet())
                .filter((entry) -> entry.getValue().size() > 1)
                .map((entry) -> pair(entry.getKey())).value();
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

    public static Group pair(Piece piece) {
        return new Group(Lists.newArrayList(piece, piece), GroupType.PAIR, piece.getKind());
    }

    public static Group triple(Piece piece) {
        return new Group(Lists.newArrayList(piece, piece, piece), GroupType.TRIPLE, piece.getKind());
    }
}
