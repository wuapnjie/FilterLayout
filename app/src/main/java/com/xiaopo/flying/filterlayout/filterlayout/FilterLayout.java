package com.xiaopo.flying.filterlayout.filterlayout;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xiaopo.flying.filterlayout.util.DeviceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 分类布局
 * Created by snowbean on 16-7-6.
 */
public class FilterLayout extends ViewGroup {

    private int mScreenWidth;
    private FilterAdapter mAdapter;
    private int mColumnCount = 6;
    private int mSelectedCount = 6;

    private View mSelectedTitle;
    private View mUnSelectedTitle;

    private boolean mIsChanged = false;

    //选中的Item
    private ArrayMap<View, FilterItem> mSelectedViews = new ArrayMap<>();
    //未选中的Item
    private ArrayMap<View, FilterItem> mUnselectedViews = new ArrayMap<>();

    private Set<Integer> mSelectedPositions = new HashSet<>();

    private String TAG = FilterLayout.class.getSimpleName();

    public FilterLayout(Context context) {
        super(context);
    }

    public FilterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FilterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = DeviceUtil.getScreenWidth(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mAdapter == null) return;

        int width = MeasureSpec.makeMeasureSpec(mScreenWidth / mColumnCount, MeasureSpec.getMode(widthMeasureSpec));
        int height = MeasureSpec.makeMeasureSpec(mScreenWidth / mColumnCount, MeasureSpec.getMode(heightMeasureSpec));

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child == null) return;
            child.measure(width, height);
        }

        if (mSelectedTitle != null) {
            mSelectedTitle.measure(widthMeasureSpec, heightMeasureSpec);
        }

        if (mUnSelectedTitle != null) {
            mUnSelectedTitle.measure(widthMeasureSpec, heightMeasureSpec);
        }

        final int selectedRow = ceil((double) (mSelectedCount) / mColumnCount);
        final int unselectedRow = ceil((double) (mAdapter.getItemCount() - mSelectedCount) / mColumnCount);

        final int row = selectedRow + unselectedRow + 1;

//        System.out.println("selectedTitleHeight->" + mSelectedTitle.getMeasuredHeight());
//        System.out.println("unselectedTitleHeight->" + mUnSelectedTitle.getMeasuredHeight());

        if (mSelectedTitle == null && mUnSelectedTitle == null) {
            setMeasuredDimension(mScreenWidth, row * mScreenWidth / mColumnCount);
        } else if (mSelectedTitle != null && mUnSelectedTitle == null) {
            setMeasuredDimension(mScreenWidth, row * mScreenWidth / mColumnCount + mSelectedTitle.getMeasuredHeight());
        } else if (mSelectedTitle == null && mUnSelectedTitle != null) {
            setMeasuredDimension(mScreenWidth, row * mScreenWidth / mColumnCount + mUnSelectedTitle.getMeasuredHeight());
        } else if (mSelectedTitle != null && mUnSelectedTitle != null) {
            setMeasuredDimension(mScreenWidth, row * mScreenWidth / mColumnCount + mSelectedTitle.getMeasuredHeight() + mUnSelectedTitle.getMeasuredHeight());
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mAdapter == null) {
            Log.e(TAG, "FilterLayout: the adapter is null,skip layout");
            return;
        }

        final int itemWidth = mScreenWidth / mColumnCount;
//        final int rowCount = ceil((double) getChildCount() / mColumnCount);

        int count = 0;

        int selectedTitleHeight = 0;
        if (mSelectedTitle != null) {
            selectedTitleHeight = mSelectedTitle.getMeasuredHeight();
            mSelectedTitle.layout(0, 0, mScreenWidth, selectedTitleHeight);
//            System.out.println("selectedTitleHeight->" + selectedTitleHeight);
        }
        final int selectedRow = ceil((double) (mSelectedCount) / mColumnCount);

        for (int i = 0; i < selectedRow; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                final View childView = getChildAt(i * mColumnCount + j);
                if (childView == null || mSelectedCount == count) break;
                count++;
                childView.layout(j * itemWidth,
                        i * itemWidth + selectedTitleHeight,
                        j * itemWidth + itemWidth,
                        i * itemWidth + itemWidth + selectedTitleHeight);
            }
        }

        int unselectedTitleHeight = 0;
        if (mUnSelectedTitle != null) {
            unselectedTitleHeight = mUnSelectedTitle.getMeasuredHeight();
            mUnSelectedTitle.layout(0,
                    selectedTitleHeight + selectedRow * itemWidth,
                    mScreenWidth,
                    selectedTitleHeight + selectedRow * itemWidth + unselectedTitleHeight);
//            System.out.println("unselectedTitleHeight->" + unselectedTitleHeight);
        }
        final int unselectedRow = ceil((double) (mAdapter.getItemCount() - mSelectedCount) / mColumnCount);

        for (int i = 0; i < unselectedRow; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                final View childView = getChildAt(count);
                if (childView == null || count == mAdapter.getItemCount()) break;
                count++;
                childView.layout(j * itemWidth,
                        i * itemWidth + selectedRow * itemWidth + selectedTitleHeight + unselectedTitleHeight,
                        j * itemWidth + itemWidth,
                        i * itemWidth + itemWidth + selectedRow * itemWidth + selectedTitleHeight + unselectedTitleHeight);
            }
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public FilterAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(FilterAdapter adapter) {
        mAdapter = adapter;
        layoutFilterItems();
    }

    public void setAdapter(FilterAdapter adapter, int selectedCount) {
        mAdapter = adapter;
        mSelectedCount = selectedCount;
        layoutFilterItems();
    }

    private void layoutFilterItems() {
        if (mAdapter == null) {
//            Log.e(TAG, "layoutFilterItems: the adapter is null");
            return;
        }

        removeAllViews();
        mSelectedViews.clear();
        mUnselectedViews.clear();
        mSelectedPositions.clear();

        int selectedItemCount = 0;
        int unselectedItemCount = 0;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            View view = mAdapter.createView(this);
            if (view != null) {
                mAdapter.bindView(view, i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIsChanged = true;
                        animateViews(v);
                    }
                });

                addView(view);

                if (i < mSelectedCount) {
                    int row = ceil((selectedItemCount + 0.5) / mColumnCount);
                    int column = selectedItemCount % mColumnCount + 1;
                    FilterItem filterItem = new FilterItem(row, column, true, i);

                    mSelectedPositions.add(i);

//                    Log.e(TAG, "layoutFilterItems: the selected item: " + filterItem.toString());

                    mSelectedViews.put(view, filterItem);
                    selectedItemCount++;
                } else {
                    int row = ceil((unselectedItemCount + 0.5) / mColumnCount);
                    int column = unselectedItemCount % mColumnCount + 1;
                    FilterItem filterItem = new FilterItem(row, column, false, i);

//                    Log.e(TAG, "layoutFilterItems: the selected item: " + filterItem.toString());
                    mUnselectedViews.put(view, filterItem);

                    unselectedItemCount++;
                }
            }

        }

        mSelectedTitle = mAdapter.createSelectedTitleView(this);
        if (mAdapter.hasSelectedTitle() && mSelectedTitle != null) {
            addView(mSelectedTitle);
        }

        mUnSelectedTitle = mAdapter.createUnSelectedTitleView(this);
        if (mAdapter.hasUnSelectedTitle() && mUnSelectedTitle != null) {
            addView(mUnSelectedTitle);
        }

        requestLayout();
    }

    private void animateViews(View v) {
        final int unselectedCount = mAdapter.getItemCount() - mSelectedCount;

        final int oldSelectedRow = ceil((double) (mSelectedCount) / mColumnCount);
//        final int oldUnselectedRow = ceil((double) (unselectedCount) / mColumnCount);

        boolean isSelected = mSelectedViews.containsKey(v);

        FilterItem clickedPosition;
        FilterItem destPosition;

        if (isSelected) {
            clickedPosition = mSelectedViews.get(v);
            int destRow = ceil((unselectedCount + 0.5) / mColumnCount);
            int destColumn = unselectedCount % mColumnCount + 1;
            destPosition = new FilterItem(destRow, destColumn, false, clickedPosition.getPosition());

            animateDown(v, clickedPosition, destPosition, oldSelectedRow);

        } else {
            clickedPosition = mUnselectedViews.get(v);
            int destRow = ceil((mSelectedCount + 0.5) / mColumnCount);
            int destColumn = mSelectedCount % mColumnCount + 1;
            destPosition = new FilterItem(destRow, destColumn, true, clickedPosition.getPosition());

            animateUp(v, clickedPosition, destPosition, oldSelectedRow);
        }

    }

    private void animateUp(View itemView, FilterItem clickedPosition, FilterItem destPosition, int oldSelectedRow) {
//        System.out.println("clicked position:" + clickedPosition);
//        System.out.println("destPosition:" + destPosition);

        mSelectedCount++;
        int newSelectedRow = ceil((double) mSelectedCount / mColumnCount);

        final int itemWidth = mScreenWidth / mColumnCount;
        final int selectedTitleHeight = mSelectedTitle == null ? 0 : mSelectedTitle.getHeight();
        final int unselectedTitleHeight = mUnSelectedTitle == null ? 0 : mUnSelectedTitle.getHeight();

        if (newSelectedRow > oldSelectedRow) {
            if (mUnSelectedTitle != null) {
                mUnSelectedTitle.animate()
                        .translationYBy(itemWidth)
                        .setDuration(200)
                        .start();
            }

            for (View view : mUnselectedViews.keySet()) {
                view.animate()
                        .translationYBy(itemWidth)
                        .setDuration(200)
                        .start();
            }
        }

        itemView.animate()
                .x((destPosition.getColumn() - 1) * itemWidth)
                .y(selectedTitleHeight
                        + (destPosition.getRow() - 1) * itemWidth)
                .setDuration(200)
                .start();

        final int clickedIndex = (clickedPosition.getRow() - 1) * mColumnCount + clickedPosition.getColumn();

        for (View view : mUnselectedViews.keySet()) {
            FilterItem viewPosition = mUnselectedViews.get(view);
            int viewIndex = (viewPosition.getRow() - 1) * mColumnCount + viewPosition.getColumn();
            if (viewIndex > clickedIndex) {
                boolean isChangeRow = viewPosition.getColumn() - 1 == 0 && viewPosition.getRow() > 1;
                int destColumn;
                int destRow;

                if (isChangeRow) {
                    destRow = viewPosition.getRow() - 1;
                    destColumn = mColumnCount;
                } else {
                    destRow = viewPosition.getRow();
                    destColumn = viewPosition.getColumn() - 1;
                }

                viewPosition.setRow(destRow);
                viewPosition.setColumn(destColumn);

//                System.out.println("view position:" + viewPosition.toString() + ",viewIndex is " + viewIndex);

                mUnselectedViews.put(view, viewPosition);

                view.animate()
                        .x((destColumn - 1) * itemWidth)
                        .y(selectedTitleHeight
                                + newSelectedRow * itemWidth
                                + unselectedTitleHeight
                                + (destRow - 1) * itemWidth)
                        .setDuration(200)
                        .start();
            }
        }

        mSelectedViews.put(itemView, destPosition);
        mUnselectedViews.remove(itemView);

        mSelectedPositions.add(clickedPosition.getPosition());
    }

    private void animateDown(View itemView, FilterItem clickedPosition, FilterItem destPosition, int oldSelectedRow) {
//        System.out.println("clicked position:" + clickedPosition);
//        System.out.println("destPosition:" + destPosition);

        mSelectedCount--;
        int newSelectedRow = ceil((double) mSelectedCount / mColumnCount);

        final int itemWidth = mScreenWidth / mColumnCount;
        final int selectedTitleHeight = mSelectedTitle.getHeight();
        final int unselectedTitleHeight = mUnSelectedTitle.getHeight();

        if (newSelectedRow < oldSelectedRow) {
            if (mUnSelectedTitle != null) {
                mUnSelectedTitle.animate()
                        .translationYBy(-itemWidth)
                        .setDuration(200)
                        .start();
            }

            for (View view : mUnselectedViews.keySet()) {
                view.animate()
                        .translationYBy(-itemWidth)
                        .setDuration(200)
                        .start();
            }
        }

        itemView.animate()
                .x((destPosition.getColumn() - 1) * itemWidth)
                .y(selectedTitleHeight
                        + newSelectedRow * itemWidth
                        + unselectedTitleHeight
                        + (destPosition.getRow() - 1) * itemWidth)
                .setDuration(200)
                .start();

        final int clickedIndex = (clickedPosition.getRow() - 1) * mColumnCount + clickedPosition.getColumn();
//        System.out.println("clicked Index is " + clickedIndex);
        for (View view : mSelectedViews.keySet()) {
            FilterItem viewPosition = mSelectedViews.get(view);
            int viewIndex = (viewPosition.getRow() - 1) * mColumnCount + viewPosition.getColumn();
            if (viewIndex > clickedIndex) {
                boolean isChangeRow = viewPosition.getColumn() - 1 == 0 && viewPosition.getRow() > 1;
                int destColumn;
                int destRow;

                if (isChangeRow) {
                    destRow = viewPosition.getRow() - 1;
                    destColumn = mColumnCount;
                } else {
                    destRow = viewPosition.getRow();
                    destColumn = viewPosition.getColumn() - 1;
                }

                viewPosition.setRow(destRow);
                viewPosition.setColumn(destColumn);

//                System.out.println("view position:" + viewPosition.toString() + ",viewIndex is " + viewIndex);

                mSelectedViews.put(view, viewPosition);

                view.animate()
                        .x((destColumn - 1) * itemWidth)
                        .y(selectedTitleHeight
                                + (destRow - 1) * itemWidth)
                        .setDuration(200)
                        .start();
            }
        }

        mUnselectedViews.put(itemView, destPosition);
        mSelectedViews.remove(itemView);

        mSelectedPositions.remove(clickedPosition.getPosition());
    }

    public int getSelectedCount() {
        return mSelectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        if (selectedCount > mAdapter.getItemCount()) {
            Log.e(TAG, "setSelectedCount: the selected count can not be greater than item count");
            return;
        }
        mSelectedCount = selectedCount;
        layoutFilterItems();
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount;
        layoutFilterItems();
    }

    //向上取整
    private int ceil(double num) {
        return (int) Math.ceil(num);
    }


    public Set<Integer> getSelectedPositions() {
        return mSelectedPositions;
    }

    public List<Integer> getUnselectedPositions() {
        List<Integer> positions = new ArrayList<>();
        for (FilterItem filterItem : mUnselectedViews.values()) {
            positions.add(filterItem.getPosition());
        }
        return positions;
    }

    public boolean isChanged() {
        return mIsChanged;
    }

    public static abstract class FilterAdapter {

        public abstract boolean hasSelectedTitle();

        public abstract boolean hasUnSelectedTitle();

        public View createSelectedTitleView(ViewGroup parent) {
            return null;
        }

        public View createUnSelectedTitleView(ViewGroup parent) {
            return null;
        }

        public abstract View createView(ViewGroup parent);

        public abstract void bindView(View view, int position);

        public abstract int getItemCount();
    }


    public static class FilterItem {
        private int row;
        private int column;
        private boolean isSelected;
        private int position;

        public FilterItem(int row, int column, boolean isSelected, int position) {
            this.row = row;
            this.column = column;
            this.isSelected = isSelected;
            this.position = position;
        }


        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        @Override
        public String toString() {
            return "row is "
                    + row
                    + ", column is "
                    + column
                    + ", the selected state is "
                    + isSelected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + row;
            result = 31 * result + column;
            result = result + (isSelected ? 1 : 0);
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FilterItem)) {
                return false;
            }

            FilterItem position = (FilterItem) o;

            return row == position.row && column == position.column && isSelected == position.isSelected;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}