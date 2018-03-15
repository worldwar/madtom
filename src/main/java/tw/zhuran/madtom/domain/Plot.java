package tw.zhuran.madtom.domain;

import com.github.underscore.$;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.util.F;

import java.util.List;

public class Plot {
    private boolean self;
    private boolean capture;
    private boolean hard;
    private boolean peng;
    private boolean suit;
    private boolean feng;
    private boolean jiang;
    private boolean beg;
    private boolean bottom;
    private boolean rush;
    private boolean fire;

    private PlotType type;
    private List<Group> groups;
    private Form form;
    private Hand hand;
    private List<Action> actions;

    public List<Group> handGroups() {
        return form.getGroups();
    }

    public List<Group> actionGroups() {
        return $.chain(actions).filter(Pieces::hasGroup).map(Action::getGroup).value();
    }

    public List<Group> allGroups() {
        return F.add(handGroups(), actionGroups());
    }

    public void trigger(TriggerType triggerType) {
        self = triggerType == TriggerType.SELF;
        capture = !Pieces.self(triggerType);
        bottom = triggerType == TriggerType.BOTTOM;
        rush = triggerType == TriggerType.RUSH;
        fire = triggerType == TriggerType.FIRE;
    }

    public boolean featured() {
        return peng || suit || feng || jiang || beg || bottom || rush || fire;
    }

    public int featuredCount() {
        return $.filter(Lists.newArrayList(peng, suit, feng, jiang, beg, bottom, rush, fire), state -> state).size();
    }

    public Group pair() {
        List<Group> pairs = $.filter(form.getGroups(), group -> group.getGroupType() == GroupType.PAIR);
        if (pairs.size() > 0) {
            return pairs.get(0);
        }
        return null;
    }

    public int base() {
        int base = 1;
        if (featured()) {
            int featuredCount = featuredCount();
            if (self) {
                base *= 15 * featuredCount;;
            } else {
                base = 10 * featuredCount;
            }
        }
        if (hard) {
            base *= 2;
        }
        return base;
    }

    public PlotType getType() {
        return type;
    }

    public void setType(PlotType type) {
        this.type = type;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public boolean isHard() {
        return hard;
    }

    public void setHard(boolean hard) {
        this.hard = hard;
    }

    public boolean isPeng() {
        return peng;
    }

    public void setPeng(boolean peng) {
        this.peng = peng;
    }

    public boolean isSuit() {
        return suit;
    }

    public void setSuit(boolean suit) {
        this.suit = suit;
    }

    public boolean isFeng() {
        return feng;
    }

    public void setFeng(boolean feng) {
        this.feng = feng;
    }

    public boolean isJiang() {
        return jiang;
    }

    public void setJiang(boolean jiang) {
        this.jiang = jiang;
    }

    public boolean isBeg() {
        return beg;
    }

    public void setBeg(boolean beg) {
        this.beg = beg;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean isRush() {
        return rush;
    }

    public void setRush(boolean rush) {
        this.rush = rush;
    }

    public boolean isFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public Hand getHand() {
        return hand;
    }

    public Plot setHand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public Shift shift() {
        if (form != null) {
            return form.getShift();
        }
        return new Shift(null);
    }

    public int countOfWildcards() {
        if (form != null) {
            return form.getShift().size();
        } else {
            return hand.getWildcards().size();
        }
    }

    @Override
    public String toString() {
        List<String> titles = Lists.newArrayList();
        List<String> names = Lists.newArrayList();
        if (featured()) {
            if (isPeng()) {
                names.add("碰碰胡");
            }
            if (isSuit()) {
                if (isFeng()) {
                    names.add("风一色");
                } else {
                    names.add("清一色");
                }
            }
            if (isJiang()) {
                names.add("将一色");
            }
            if (isBeg()) {
                names.add("全求人");
            }
            if (isBottom()) {
                names.add("海底捞");
            }
            if (isFire()) {
                names.add("杠上开");
            }
            if (isRush()) {
                names.add("抢杠");
            }
        } else {
            names.add("屁胡");
        }
        if (isHard()) {
            titles.add("硬胡");
        } else {
            titles.add("软胡");
        }
        if (isSelf()) {
            titles.add("自摸");
        }
        if (isCapture()) {
            titles.add("放冲");
        }
        titles.addAll(names);
        return F.string(titles, " ");
    }
}
