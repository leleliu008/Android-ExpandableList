package com.fpliu.newton.ui.expandablelist;

import android.content.Context;
import android.util.AttributeSet;


/**
 * @author 792793182@qq.com 2016-07-30.
 */
public class PullablePinnedHeaderExpandableListView extends PinnedHeaderExpandableListView {

    private boolean canPullUp_ = true;

    private boolean canPullDown_ = true;

    public PullablePinnedHeaderExpandableListView(Context context) {
        super(context);
    }

    public PullablePinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullablePinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void canPullUp(boolean canPullUp) {
        this.canPullUp_ = canPullUp;
    }

    public void canPullDown(boolean canPullDown) {
        this.canPullDown_ = canPullDown;
    }

    public boolean canPullDown() {
        if (canPullDown_) {
            if (getCount() == 0) {
                // 没有item的时候也可以下拉刷新
                return true;
            } else if (getChildAt(0) != null) {
                if (getFirstVisiblePosition() == 0

                        && getChildAt(0).getTop() >= 0) {
                    // 滑到ListView的顶部了
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canPullUp() {
        if (canPullUp_) {
            if (getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (getLastVisiblePosition() == (getCount() - 1)) {
                // 滑到底部了
                if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                        && getChildAt(
                        getLastVisiblePosition()
                                - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()) {
                    return true;
                }
            }
        }
        return false;
    }

}
