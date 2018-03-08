package tw.zhuran.madtom.domain;

import com.github.underscore.$;

import java.util.List;
import java.util.Map;

public class Plot {
    private boolean self;
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
    private List<Map<Piece, Piece>> shifts;
    private List<Group> groups;
    private Form form;
    private List<Action> actions;

    public List<Group> handGroups() {
        return form.getGroups();
    }

    public List<Group> actionGroups() {
        return $.chain(actions).filter(Pieces::hasGroup).map(Action::getGroup).value();
    }

    public List<Group> allGroups() {
        return Pieces.add(handGroups(), actionGroups());
    }

    public void trigger(TriggerType triggerType) {
        self = Pieces.self(triggerType);
        bottom = triggerType == TriggerType.BOTTOM;
        rush = triggerType == TriggerType.RUSH;
        fire = triggerType == TriggerType.FIRE;
    }

    public PlotType getType() {
        return type;
    }

    public void setType(PlotType type) {
        this.type = type;
    }

    public List<Map<Piece, Piece>> getShifts() {
        return shifts;
    }

    public void setShifts(List<Map<Piece, Piece>> shifts) {
        this.shifts = shifts;
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
}
