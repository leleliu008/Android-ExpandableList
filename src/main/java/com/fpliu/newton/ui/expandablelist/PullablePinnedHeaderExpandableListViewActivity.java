package com.fpliu.newton.ui.expandablelist;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.fpliu.newton.ui.base.BaseActivity;
import com.fpliu.newton.ui.pullable.PullableViewContainer;
import com.fpliu.newton.ui.pullable.RefreshOrLoadMoreCallback;

import java.util.List;

/**
 * @author 792793182@qq.com 2016-07-28.
 */
public abstract class PullablePinnedHeaderExpandableListViewActivity<T extends Group_> extends BaseActivity implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        PinnedHeaderExpandableListView.OnHeaderUpdateListener,
        RefreshOrLoadMoreCallback<PullablePinnedHeaderExpandableListView> {

    private PullableViewContainer<PullablePinnedHeaderExpandableListView> pullableViewContainer;

    private ExpandableItemAdapter<T> itemAdapter;

    private LinearLayout bodyBeforePanel;

    private LinearLayout bodyAfterPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        bodyBeforePanel = new LinearLayout(this);
        bodyBeforePanel.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(bodyBeforePanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        pullableViewContainer = new PullableViewContainer<>(this, PullablePinnedHeaderExpandableListView.class);
        linearLayout.addView(pullableViewContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        PullablePinnedHeaderExpandableListView expandableListView = pullableViewContainer.getPullableView();
        expandableListView.setCacheColorHint(Color.TRANSPARENT);
        expandableListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        expandableListView.setChildDivider(new ColorDrawable(Color.TRANSPARENT));
        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        expandableListView.setDividerHeight(0);

        if (itemAdapter == null) {
            itemAdapter = new ExpandableItemAdapter<T>(null) {
                @Override
                public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                    return PullablePinnedHeaderExpandableListViewActivity.this.getGroupView(groupPosition, isExpanded, convertView, parent);
                }

                @Override
                public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                    return PullablePinnedHeaderExpandableListViewActivity.this.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
                }
            };
        }
        expandableListView.setAdapter(itemAdapter);
        expandableListView.setOnHeaderUpdateListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);

        bodyAfterPanel = new LinearLayout(this);
        bodyAfterPanel.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(bodyAfterPanel, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        addContentView(linearLayout);

        pullableViewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pullableViewContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                pullableViewContainer.setRefreshOrLoadMoreCallback(PullablePinnedHeaderExpandableListViewActivity.this);
            }
        });
    }

    protected final PullableViewContainer<PullablePinnedHeaderExpandableListView> getPullableViewContainer() {
        return pullableViewContainer;
    }

    protected final void setItemAdapter(ExpandableItemAdapter<T> itemAdapter) {
        this.itemAdapter = itemAdapter;

        if (pullableViewContainer != null) {
            pullableViewContainer.getPullableView().setAdapter(itemAdapter);
        }
    }

    protected final ExpandableItemAdapter<?> getExpandableItemAdapter() {
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

    public void setBodyBeforeView(int layoutId) {
        bodyBeforePanel.addView(LayoutInflater.from(this).inflate(layoutId, bodyBeforePanel, false));
    }

    public void setBodyBeforeView(View bodyBeforeView) {
        bodyBeforePanel.addView(bodyBeforeView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    public void setBodyAfterView(int layoutId) {
        bodyAfterPanel.addView(LayoutInflater.from(this).inflate(layoutId, bodyAfterPanel, false));
    }

    public void setBodyAfterView(View bodyBeforeView) {
        bodyAfterPanel.addView(bodyBeforeView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    protected final T getGroup(int groupPosition) {
        return itemAdapter == null ? null : itemAdapter.getGroup(groupPosition);
    }

    protected final <T> T getChild(int groupPosition, int childPosition) {
        return itemAdapter == null ? null : (T) itemAdapter.getChild(groupPosition, childPosition);
    }

    protected final int getChildrenCount(int groupPosition) {
        return itemAdapter == null ? null : itemAdapter.getChildrenCount(groupPosition);
    }

    protected final boolean expandGroup(int groupPos) {
        return itemAdapter == null ? null : pullableViewContainer.getPullableView().expandGroup(groupPos);
    }

    protected final void canPullDown(boolean canPullDown) {
        pullableViewContainer.getPullableView().canPullDown(canPullDown);
    }

    protected final void canPullUp(boolean canPullUp) {
        pullableViewContainer.getPullableView().canPullUp(canPullUp);
    }
}