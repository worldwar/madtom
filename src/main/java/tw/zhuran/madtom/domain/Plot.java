package tw.zhuran.madtom.domain;

import java.util.List;
import java.util.Map;

public class Plot {
    private PlotType type;
    private List<Map<Piece, Piece>> shifts;
    private List<Group> groups;
    private List<Action> actions;

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

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
