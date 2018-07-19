package com.fpliu.newton.ui.expandablelist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.fpliu.newton.ui.base.BaseView;
import com.fpliu.newton.ui.base.LazyFragment;
import com.fpliu.newton.ui.pullable.PullableViewContainer;
import com.fpliu.newton.ui.pullable.RefreshOrLoadMoreCallback;
import com.fpliu.newton.ui.stateview.StateView;

import java.util.List;

/**
 * @author 792793182@qq.com 2016-07-28.
 */
public abstract class PullablePinnedHeaderExpandableListViewFragment<Child, T extends Group_<Child>> extends LazyFragment implements
    ExpandableListView.OnChildClickListener,
    ExpandableListView.OnGroupClickListener,
    PinnedHeaderExpandableListView.OnHeaderUpdateListener,
    RefreshOrLoadMoreCallback<PinnedHeaderExpandableListView> {

    private LinearLayout bodyBeforePanel;

    private PullableViewContainer<PinnedHeaderExpandableListView> pullableViewContainer;

    private ExpandableItemAdapter<Child, T> itemAdapter;

    @Override
    protected void onCreateViewLazy(BaseView baseView, Bundle savedInstanceState) {
        super.onCreateViewLazy(baseView, savedInstanceState);

        Context context = getActivity();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        bodyBeforePanel = new LinearLayout(context);
        bodyBeforePanel.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(bodyBeforePanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        pullableViewContainer = new PullableViewContainer<>(PinnedHeaderExpandableListView.class, new StateView(getActivity()));
        pullableViewContainer.setDefaultLayout();
        linearLayout.addView(pullableViewContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        PinnedHeaderExpandableListView expandableListView = pullableViewContainer.getPullableView();
        expandableListView.setCacheColorHint(Color.TRANSPARENT);
        expandableListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        expandableListView.setChildDivider(new ColorDrawable(Color.TRANSPARENT));
        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        expandableListView.setDividerHeight(0);

        if (itemAdapter == null) {
            itemAdapter = new ExpandableItemAdapter<Child, T>(null) {
                @Override
                public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                    return PullablePinnedHeaderExpandableListViewFragment.this.getGroupView(groupPosition, isExpanded, convertView, parent);
                }

                @Override
                public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                    return PullablePinnedHeaderExpandableListViewFragment.this.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                }
            };
        }
        expandableListView.setAdapter(itemAdapter);
        expandableListView.setOnHeaderUpdateListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);

        baseView.addView(linearLayout);

        pullableViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pullableViewContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                pullableViewContainer.setRefreshOrLoadMoreCallback(PullablePinnedHeaderExpandableListViewFragment.this);
            }
        });
    }

    protected final PullableViewContainer<PinnedHeaderExpandableListView> getPullableViewContainer() {
        return pullableViewContainer;
    }

    protected final void setItemAdapter(ExpandableItemAdapter<Child, T> itemAdapter) {
        this.itemAdapter = itemAdapter;

        if (pullableViewContainer != null) {
            pullableViewContainer.getPullableView().setAdapter(itemAdapter);
        }
    }

    public void setBodyBeforeView(int layoutId) {
        bodyBeforePanel.addView(LayoutInflater.from(getContext()).inflate(layoutId, bodyBeforePanel, false));
    }

    public void setBodyBeforeView(View bodyBeforeView) {
        bodyBeforePanel.addView(bodyBeforeView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    protected final ExpandableItemAdapter<Child, T> getExpandableItemAdapter() {
        return itemAdapter;
    }

    protected final void refresh() {

    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    protected final void setGroupItems(List<T> groups) {
        itemAdapter.setGroupItems(groups);
    }

    protected final void addGroupItems(List<T> groups) {
        itemAdapter.addGroupItems(groups);
    }

    protected final T getGroup(int groupPosition) {
        return itemAdapter == null ? null : itemAdapter.getGroup(groupPosition);
    }

    protected final Child getChild(int groupPosition, int childPosition) {
        return itemAdapter == null ? null : itemAdapter.getChild(groupPosition, childPosition);
    }

    protected final int getChildrenCount(int groupPosition) {
        return itemAdapter == null ? null : itemAdapter.getChildrenCount(groupPosition);
    }

    protected final boolean expandGroup(int groupPos) {
        return itemAdapter == null ? null : pullableViewContainer.getPullableView().expandGroup(groupPos);
    }

    protected final void canPullDown(boolean canPullDown) {
        pullableViewContainer.setEnableRefresh(canPullDown);
    }

    protected final void canPullUp(boolean canPullUp) {
        pullableViewContainer.setEnableLoadmore(canPullUp);
    }
}
