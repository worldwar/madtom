package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HandTest {
    private Piece yiwan = new Piece(Kind.WAN, 1);
    private Piece erwan = new Piece(Kind.WAN, 2);
    private Piece sanwan = new Piece(Kind.WAN, 3);
    private Piece siwan = new Piece(Kind.WAN, 4);
    private Piece wuwan = new Piece(Kind.WAN, 5);
    private Piece liuwan = new Piece(Kind.WAN, 6);
    private Piece qiwan = new Piece(Kind.WAN, 7);
    private Piece bawan = new Piece(Kind.WAN, 8);
    private Piece jiuwan = new Piece(Kind.WAN, 9);

    private Piece yitiao = new Piece(Kind.TIAO, 1);
    private Piece ertiao = new Piece(Kind.TIAO, 2);
    private Piece santiao = new Piece(Kind.TIAO, 3);
    private Piece sitiao = new Piece(Kind.TIAO, 4);
    private Piece wutiao = new Piece(Kind.TIAO, 5);
    private Piece liutiao = new Piece(Kind.TIAO, 6);
    private Piece qitiao = new Piece(Kind.TIAO, 7);
    private Piece batiao = new Piece(Kind.TIAO, 8);
    private Piece jiutiao = new Piece(Kind.TIAO, 9);

    private Piece yitong = new Piece(Kind.TONG, 1);
    private Piece ertong = new Piece(Kind.TONG, 2);
    private Piece santong = new Piece(Kind.TONG, 3);
    private Piece sitong = new Piece(Kind.TONG, 4);
    private Piece wutong = new Piece(Kind.TONG, 5);
    private Piece liutong = new Piece(Kind.TONG, 6);
    private Piece qitong = new Piece(Kind.TONG, 7);
    private Piece batong = new Piece(Kind.TONG, 8);
    private Piece jiutong = new Piece(Kind.TONG, 9);

    private Piece dongfeng = new Piece(Kind.FENG, 1);
    private Piece nanfeng = new Piece(Kind.FENG, 2);
    private Piece xifeng = new Piece(Kind.FENG, 3);
    private Piece beifeng = new Piece(Kind.FENG, 4);
    private Piece hongzhong = new Piece(Kind.FENG, 5);
    private Piece facai = new Piece(Kind.FENG, 6);
    private Piece baiban = new Piece(Kind.FENG, 7);

    private Hand hand;

    @Before
    public void before() {
        hand = new Hand();
    }

    @Test
    public void testComplete() throws Exception {
        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, yiwan, erwan, sanwan, siwan, wuwan, liuwan, qiwan, bawan, jiuwan, jiuwan,jiuwan,jiuwan));
        assertTrue(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, yiwan, erwan, erwan, erwan, sanwan, sanwan, sanwan, wuwan, wuwan, liuwan,jiuwan,jiuwan));
        assertFalse(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, siwan, wuwan, liuwan, qiwan, bawan, jiuwan, jiuwan,jiuwan,jiuwan));
        hand.setFengPieces(Lists.newArrayList(dongfeng, nanfeng, xifeng));
        assertFalse(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, siwan, wuwan, liuwan, qiwan, bawan, jiuwan, jiuwan,jiuwan,jiuwan));
        hand.setFengPieces(Lists.newArrayList(dongfeng, dongfeng, dongfeng));
        assertTrue(hand.complete());
    }

    @Test
    public void testComplteWhenCountOfPiecesIsNot14() {
        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, erwan, erwan, sanwan, sanwan, wuwan, wuwan));
        assertTrue(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, erwan, erwan, sanwan, sanwan, wuwan, wuwan, wuwan));
        assertFalse(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan));
        assertTrue(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, erwan));
        assertFalse(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan));
        assertFalse(hand.complete());
    }

    @Test
    public void testCompleteWithAllKinds() {
        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, erwan, erwan, sanwan, sanwan));
        hand.setTiaoPieces(Lists.newArrayList(yitiao, yitiao));
        hand.setTongPieces(Lists.newArrayList(yitong, yitong, yitong));
        hand.setFengPieces(Lists.newArrayList(dongfeng, dongfeng, dongfeng));
        assertTrue(hand.complete());

        hand.setWanPieces(Lists.newArrayList(yiwan, yiwan, erwan, erwan, sanwan, sanwan, siwan, siwan));
        hand.setTiaoPieces(Lists.newArrayList(yitiao, yitiao));
        hand.setTongPieces(Lists.newArrayList(yitong, yitong));
        hand.setFengPieces(Lists.newArrayList(dongfeng, dongfeng));
        assertFalse(hand.complete());
    }
}
