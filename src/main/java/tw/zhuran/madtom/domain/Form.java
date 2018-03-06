package tw.zhuran.madtom.domain;

import com.google.common.collect.Lists;

import java.util.List;

public class Form {
    private List<Group> groups = Lists.newArrayList();
    private Shift shift;

    public Form() {
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void add(Group group) {
        groups.add(group);
    }

    public Form copy() {
        Form form = new Form();
        form.setGroups(Lists.newArrayList(groups));
        return form;
    }
}
