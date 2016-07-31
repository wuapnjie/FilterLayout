package com.xiaopo.flying.filterlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaopo.flying.filterlayout.filterlayout.FilterLayout;

import java.util.List;

/**
 * Created by snowbean on 16-7-6.
 */
public class MyFilterTextAdapter extends FilterLayout.FilterAdapter {
    private final List<String> mData;

    public MyFilterTextAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public boolean hasSelectedTitle() {
        return true;
    }

    @Override
    public boolean hasUnSelectedTitle() {
        return true;
    }

    @Override
    public View createSelectedTitleView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_selected_title, parent, false);
    }

    @Override
    public View createUnSelectedTitleView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_unselected_title, parent, false);
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_text, parent, false);
    }

    @Override
    public void bindView(View view, int position) {
        TextView textView = (TextView) view.findViewById(R.id.tv_filter);
        textView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

}
