package com.fpliu.newton.ui.expandablelist;

import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * @author 792793182@qq.com 2016-07-28.
 */
public abstract class ExpandableItemAdapter<Group, Child> extends BaseExpandableListAdapter {

    private List<Group> groups;

    public ExpandableItemAdapter(List<Group> groups) {
        this.groups = groups;
    }

    public void setGroupItems(List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    public void addGroupItems(List<Group> groups) {
        if (this.groups == null) {
            this.groups = groups;
        } else {
            groups.addAll(groups);
        }
        this.groups = groups;
        notifyDataSetChanged();
    }


    // 返回父列表个数
    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
    }

    // 返回子列表个数
    @Override
    public int getChildrenCount(int groupPosition) {
        if (groups == null) {
            return 0;
        }

        Group group = groups.get(groupPosition);
        if (group == null) {
            return 0;
        }

        List<Child> children = getChildren(group);
        if (children == null) {
            return 0;
        }

        return children.size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return groups == null ? null : groups.get(groupPosition);
    }

    @Override
    public Child getChild(int groupPosition, int childPosition) {
        if (groups == null) {
            return null;
        }

        Group group = groups.get(groupPosition);
        if (group == null) {
            return null;
        }

        List<Child> children = getChildren(group);
        if (children == null) {
            return null;
        }

        return children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public abstract List<Child> getChildren(Group group);
}
